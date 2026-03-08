package io.github.defective4.sdr.owrxsrc.model.server.message;

public class DisconnectMessage extends ServerMessage {

    private final String reason;

    public DisconnectMessage(String reason) {
        super("backoff");
        this.reason = reason;
    }

}
