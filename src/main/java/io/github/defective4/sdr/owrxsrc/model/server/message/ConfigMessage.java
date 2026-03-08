package io.github.defective4.sdr.owrxsrc.model.server.message;

import io.github.defective4.sdr.owrxsrc.model.ServerConfig;

public class ConfigMessage extends ServerMessage {
    private final ServerConfig value;

    public ConfigMessage(ServerConfig value) {
        super("config");
        this.value = value;
    }
}
