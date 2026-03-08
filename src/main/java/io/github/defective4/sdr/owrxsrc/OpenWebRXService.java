package io.github.defective4.sdr.owrxsrc;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.http.HttpStatus.*;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.defective4.sdr.owrxsrc.model.ServiceDetails;
import io.github.defective4.sdr.owrxsrc.model.client.message.ClientMessageType;
import io.github.defective4.sdr.owrxsrc.model.server.message.ServerChatMessage;
import io.github.defective4.sdr.owrxsrc.session.ClientSession;
import io.github.defective4.sdr.owrxsrc.session.ClientSessionManager;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateManager;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateRenderer;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinGson;
import io.javalin.websocket.WsMessageContext;

public class OpenWebRXService {

    private static final String HS_BRAND = "openwebrx";

    private static final String HS_CLIENT_HEADER = "SERVER DE CLIENT ";

    private static final String HS_SERVER_HEADER = "CLIENT DE SERVER server=%s version=%s";

    private static final String OWRX_RES = "/owrx/htdocs";

    private final AssetCompiler compiler;

    private final Gson gson = new Gson();
    private final MessageHandler handler = new MessageHandler(this);
    private final Javalin javalin;
    private final Logger mainLogger = new SimpleLoggerFactory().getLogger("owrx-backend");
    private final ServiceDetails serviceDetails;
    private final ClientSessionManager sessionManager = new ClientSessionManager();
    private final HTMLTemplateManager templateManager = new HTMLTemplateManager();
    private final String version = "v1.2.108"; // TODO load externally

    public OpenWebRXService(ServiceDetails serviceDetails) throws IOException {
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
                get("/", ctx -> ctx.render(OWRX_RES + "/index.html", Map.of("header",
                        templateManager.renderTemplate(OWRX_RES + "/include/header.include.html", serviceDetails))));
                get("/compiled/{asset}", ctx -> {
                    Optional<String> asset = compiler.getCompiledAsset(ctx.pathParam("asset"));
                    if (asset.isPresent()) {
                        ctx.result(asset.get());
                    } else {
                        ctx.status(NOT_FOUND);
                    }
                });
                ws("/ws/", wcfg -> {
                    wcfg.onClose(ctx -> sessionManager.remove(ctx.sessionId()));
                    wcfg.onError(ctx -> sessionManager.remove(ctx.sessionId()));
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
                                    log(ctx, "Unknown message received: {}", obj.toString());
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
                    });
                    wcfg.onConnect(ctx -> sessionManager.add(ctx.sessionId(),
                            new ClientSession(ctx.sessionId(), ctx.session)));
                });
            });
        });
        this.serviceDetails = serviceDetails;
    }

    public void broadcastChatMessage(String from, String text, Color color) {
        sessionManager.broadcastMessage(new ServerChatMessage(from, text,
                String.format("#%s%s%s", toHex(color.getRed()), toHex(color.getGreen()), toHex(color.getBlue()))));
    }

    public Javalin start(int port) {
        return javalin.start(port);
    }

    private void debug(WsMessageContext ctx, String message, String... args) {
        mainLogger.debug(String.format("[{}] %s", message), mergeArguments(ctx, args));
    }

    private void log(WsMessageContext ctx, String message, String... args) {
        mainLogger.info(String.format("[{}] %s", message), mergeArguments(ctx, args));
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

    private static String toHex(int i) {
        String hex = Integer.toHexString(i);
        if (hex.length() > 2) hex = hex.substring(0, 2);
        if (hex.length() < 2) hex = "0" + hex;
        return hex;
    }

}
