package io.github.defective4.sdr.owrxsrc.model.server.message;

public class ConfigMessage extends ServerMessage {
    private final Record value;

    public ConfigMessage(Record value) {
        super("config");
        this.value = value;
    }
}
