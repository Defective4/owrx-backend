package io.github.defective4.sdr.owrxsrc;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.http.HttpStatus.*;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.defective4.sdr.owrxsrc.model.Features;
import io.github.defective4.sdr.owrxsrc.model.Mode;
import io.github.defective4.sdr.owrxsrc.model.Modulation;
import io.github.defective4.sdr.owrxsrc.model.ReceiverDetails;
import io.github.defective4.sdr.owrxsrc.model.ServiceDetails;
import io.github.defective4.sdr.owrxsrc.model.client.message.ClientMessageType;
import io.github.defective4.sdr.owrxsrc.model.config.PrimaryServerConfig;
import io.github.defective4.sdr.owrxsrc.model.config.SecondaryServerConfig;
import io.github.defective4.sdr.owrxsrc.model.server.message.ChatMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ClientCountMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ConfigMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.FeaturesMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ModesMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ProfilesMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ReceiverDetailsMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.SecondaryConfigMessage;
import io.github.defective4.sdr.owrxsrc.sdr.Receiver;
import io.github.defective4.sdr.owrxsrc.sdr.ReceiverBand;
import io.github.defective4.sdr.owrxsrc.sdr.Receivers;
import io.github.defective4.sdr.owrxsrc.session.ClientSession;
import io.github.defective4.sdr.owrxsrc.session.ClientSessionManager;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateManager;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateRenderer;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinGson;
import io.javalin.websocket.WsMessageContext;

public class OpenWebRXService {

    public static final Gson GSON = new Gson();

    private static final String HS_BRAND = "openwebrx";

    private static final String HS_CLIENT_HEADER = "SERVER DE CLIENT ";

    private static final String HS_SERVER_HEADER = "CLIENT DE SERVER server=%s version=%s";

    private static final String OWRX_RES = "/owrx/htdocs";

    private final AssetCompiler compiler;
    private final OWRXConfiguration config;
    private final MessageHandler handler = new MessageHandler(this);
    private final Javalin javalin;
    private final Logger mainLogger = new SimpleLoggerFactory().getLogger("owrx-backend");
    private final String[] motd = { "Welcome to OWRX Backend!",
            "Check the code at https://github.com/Defective4/owrx-backend" };
    private final Receivers receivers = new Receivers();
    private final ReceiverDetails recvDetails;
    private final ClientSessionManager sessionManager = new ClientSessionManager();
    private final HTMLTemplateManager templateManager = new HTMLTemplateManager();
    private final String version = "v1.2.108"; // TODO load externally

    public OpenWebRXService(ReceiverDetails recvDetails, OWRXConfiguration config) throws IOException {
        compiler = new AssetCompiler(OWRX_RES);
        javalin = Javalin.create(cfg -> {
            cfg.jsonMapper(new JavalinGson());
            cfg.fileRenderer(new HTMLTemplateRenderer(templateManager));
            cfg.staticFiles.add(scfg -> {
                scfg.location = Location.CLASSPATH;
                scfg.directory = OWRX_RES;
                scfg.hostedPath = "/static";
            });
            cfg.router.apiBuilder(() -> {
                get("/", ctx -> ctx.render(OWRX_RES + "/index.html",
                        Map.of("header", templateManager.renderTemplate(OWRX_RES + "/include/header.include.html",
                                new ServiceDetails(recvDetails, "/")))));
                get("/compiled/{asset}", ctx -> {
                    Optional<String> asset = compiler.getCompiledAsset(ctx.pathParam("asset"));
                    if (asset.isPresent()) {
                        ctx.result(asset.get());
                    } else {
                        ctx.status(NOT_FOUND);
                    }
                });
                ws("/ws/", wcfg -> {
                    wcfg.onClose(ctx -> {
                        sessionManager.remove(ctx.sessionId());
                        updateClientCount();
                    });
                    wcfg.onError(ctx -> {
                        sessionManager.remove(ctx.sessionId());
                        updateClientCount();
                    });
                    wcfg.onMessage(ctx -> {
                        Optional<ClientSession> sesOptional = sessionManager.get(ctx.sessionId());
                        if (!sesOptional.isPresent()) {
                            ctx.session.disconnect();
                            return;
                        }
                        ClientSession session = sesOptional.get();
                        if (!session.hasHandshakeCompleted()) {
                            String msg = ctx.message();
                            if (msg.startsWith(HS_CLIENT_HEADER)) {
                                String[] parts = msg.substring(HS_CLIENT_HEADER.length()).split(" ");
                                Map<String, String> properties = new HashMap<>();
                                for (String part : parts) {
                                    String[] kv = part.split("=");
                                    if (kv.length < 2) continue;
                                    String key = kv[0];
                                    String value = String.join(" ", Arrays.copyOfRange(kv, 1, kv.length));
                                    properties.put(key, value);
                                }
                                String clientId = properties.get("client");
                                String type = properties.get("type");
                                if (clientId != null && type != null) {
                                    session.setClientId(clientId);
                                    session.setClientType(type);
                                    ctx.send(String.format(HS_SERVER_HEADER, HS_BRAND, version));
                                    log(ctx, "Connection received. Client ID: {}, Client Type: {}", clientId, type);
                                    session.sendMessage(new ReceiverDetailsMessage(recvDetails));
                                    session.sendMessage(new ConfigMessage(new PrimaryServerConfig(config)));
                                    changeProfile(session);
                                    session.sendMessage(new SecondaryConfigMessage(new SecondaryServerConfig(2048)));
                                    session.sendMessage(new FeaturesMessage(new Features(true)));
                                    session.sendMessage(new ModesMessage(getAvailableModes()));
                                    session.sendMessage(new ProfilesMessage(receivers.getProfiles()));
                                    for (String line : motd) {
                                        session.sendMessage(new ChatMessage("owrx-backend", line, toHex(Color.green)));
                                    }
                                    updateClientCount();
                                    return;
                                }
                                warn(ctx, "Invalid client disconnected: Incomplete header received");
                            } else {
                                warn(ctx, "Invalid client disconnected: Invalid header received");
                            }
                            ctx.session.disconnect();
                        } else {
                            try {
                                JsonObject obj = JsonParser.parseString(ctx.message()).getAsJsonObject();
                                String type = obj.get("type").getAsString();
                                Record message;
                                try {
                                    message = ctx.messageAsClass(
                                            ClientMessageType.valueOf(type.toUpperCase()).getMessageClass());
                                } catch (IllegalArgumentException e) {
                                    message = null;
                                }
                                if (message != null) {
                                    handler.handleMessage(session, message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ctx.session.disconnect();
                            }
                        }
                        System.out.println(ctx.message());
                    });
                    wcfg.onConnect(ctx -> sessionManager.add(ctx.sessionId(),
                            new ClientSession(ctx.sessionId(), ctx.session)));
                });
            });
        });
        this.recvDetails = recvDetails;
        this.config = config;

        new Timer(true).scheduleAtFixedRate(new TimerTask() {

            private final Random rand = new Random();

            @Override
            public void run() {
                float[] fft = new float[config.fftSize()];
                float val = config.waterfallLevels().min();
                boolean up = true;
                for (int i = 0; i < fft.length; i++) {
                    val += up ? 0.1 : -0.1;
                    if (val >= config.waterfallLevels().max()) {
                        up = false;
                    }

                    if (val <= config.waterfallLevels().min()) {
                        up = true;
                    }
                    fft[i] = val;
                }
                broadcastFFT(fft);
            }
        }, 200, 200);
    }

    public void broadcastChatMessage(String from, String text, Color color) {
        sessionManager.broadcastMessage(new ChatMessage(from, text, toHex(color)));
    }

    public void broadcastData(byte id, byte[] data) {
        sessionManager.broadcastData(id, data);
    }

    public void broadcastFFT(float[] fft) {
        if (fft.length != config.fftSize()) return;
        ByteBuffer fftBuffer = ByteBuffer.allocate(fft.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (float f : fft) fftBuffer.putFloat(f);
        broadcastData((byte) 0x01, fftBuffer.array());
    }

    public Mode[] getAvailableModes() {
        return getAvailableModulations().stream().map(mod -> new Mode(mod)).toArray(Mode[]::new);
    }

    public List<Modulation> getAvailableModulations() {
        return Arrays.asList(Modulation.values());
    }

    public Receivers getReceivers() {
        return receivers;
    }

    public Javalin start(int port) {
        return javalin.start(port);
    }

    private void changeProfile(ClientSession session) throws IOException {
        UUID receiverId = receivers.getSelectedReceiver();
        Receiver receiver = receivers.getReceivers().get(receiverId);
        if (receiver == null) return;
        UUID bandId = receivers.getSelectedBand();
        ReceiverBand receiverBand = receiver.getBands().get(bandId);
        if (receiverBand == null) return;

        session.sendMessage(new ConfigMessage(receiverBand.toConfig(bandId, Modulation.WFM.getMod(), receiverId)));
    }

    private void debug(WsMessageContext ctx, String message, String... args) {
        mainLogger.debug(String.format("[{}] %s", message), mergeArguments(ctx, args));
    }

    private void log(WsMessageContext ctx, String message, String... args) {
        mainLogger.info(String.format("[{}] %s", message), mergeArguments(ctx, args));
    }

    private void updateClientCount() {
        sessionManager.broadcastMessage(new ClientCountMessage(sessionManager.size()));
    }

    private void warn(WsMessageContext ctx, String message, String... args) {
        mainLogger.warn(String.format("[{}] %s", message), mergeArguments(ctx, args));
    }

    private static Object[] mergeArguments(WsMessageContext ctx, String... args) {
        Object[] merged = new String[args.length + 1];
        merged[0] = ctx.session.getRemoteAddress().toString();
        System.arraycopy(args, 0, merged, 1, args.length);
        return merged;
    }

    private static String toHex(Color color) {
        return String.format("#%s%s%s", toHex(color.getRed()), toHex(color.getGreen()), toHex(color.getBlue()));
    }

    private static String toHex(int i) {
        String hex = Integer.toHexString(i);
        if (hex.length() > 2) hex = hex.substring(0, 2);
        if (hex.length() < 2) hex = "0" + hex;
        return hex;
    }

}
