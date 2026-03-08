package io.github.defective4.sdr.owrxsrc.session;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

import io.github.defective4.sdr.owrxsrc.OpenWebRXService;
import io.github.defective4.sdr.owrxsrc.model.server.message.DisconnectMessage;
import io.github.defective4.sdr.owrxsrc.model.server.message.ServerMessage;

public class ClientSession {
    private String clientId, clientType;
    private final String id;
    private final Map<String, String> properties = new HashMap<>();
    private final Session session;

    public ClientSession(String id, Session session) {
        this.id = id;
        this.session = session;
    }

    public void disconnect(String reason) throws IOException {
        sendMessage(new DisconnectMessage(reason));
        session.close();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientType() {
        return clientType;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public Session getWSSession() {
        return session;
    }

    public boolean hasHandshakeCompleted() {
        return clientId != null && clientType != null;
    }

    public void sendMessage(ServerMessage message) throws IOException {
        session.getRemote().sendString(OpenWebRXService.GSON.toJson(message));
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientType(String type) {
        clientType = type;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
    }

}
