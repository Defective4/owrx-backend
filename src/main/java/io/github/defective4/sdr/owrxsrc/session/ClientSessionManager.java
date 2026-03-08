package io.github.defective4.sdr.owrxsrc.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientSessionManager {
    private final Map<String, ClientSession> map = new HashMap<>();

    public void add(String key, ClientSession value) {
        map.put(key, value);
    }

    public Optional<ClientSession> get(String key) {
        return Optional.ofNullable(map.get(key));
    }

    public void remove(String id) {
        map.remove(id);
    }

}
