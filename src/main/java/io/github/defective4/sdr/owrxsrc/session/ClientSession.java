package io.github.defective4.sdr.owrxsrc.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

public class ClientSession {
    private String clientId, clientType;
    private final String id;
    private final Map<String, String> properties = new HashMap<>();
    private final Session session;

    public ClientSession(String id, Session session) {
        this.id = id;
        this.session = session;
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
