package io.github.defective4.sdr.owrxsrc.model.server.message;

public class SecondaryConfigMessage extends ServerMessage {
    private final Record value;

    public SecondaryConfigMessage(Record value) {
        super("secondary_config");
        this.value = value;
    }
}
