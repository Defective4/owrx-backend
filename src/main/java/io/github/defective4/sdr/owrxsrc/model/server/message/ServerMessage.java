package io.github.defective4.sdr.owrxsrc.model.server.message;

public class ServerMessage {
    private final String type;

    protected ServerMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
