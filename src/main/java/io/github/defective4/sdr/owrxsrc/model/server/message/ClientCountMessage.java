package io.github.defective4.sdr.owrxsrc.model.server.message;

public class ClientCountMessage extends ServerMessage {
    private final int value;

    public ClientCountMessage(int value) {
        super("clients");
        this.value = value;
    }
}
