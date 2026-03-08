package io.github.defective4.sdr.owrxsrc.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import io.github.defective4.sdr.owrxsrc.model.server.message.ServerMessage;

public class ClientSessionManager {
    private final Gson gson = new Gson();

    private final Map<String, ClientSession> map = new HashMap<>();

    public void add(String key, ClientSession value) {
        synchronized (map) {
            map.put(key, value);
        }
    }

    public void broadcastMessage(ServerMessage message) {
        synchronized (map) {
            String json = gson.toJson(message);
            map.values().forEach(client -> {
                try {
                    client.getWSSession().getRemote().sendString(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public Optional<ClientSession> get(String key) {
        synchronized (map) {
            return Optional.ofNullable(map.get(key));
        }
    }

    public void remove(String id) {
        synchronized (map) {
            map.remove(id);
        }
    }

}
