package io.github.defective4.sdr.owrxsrc.model.client.message;

public enum ClientMessageType {
    SENDMESSAGE(ClientChatMessage.class);

    private final Class<? extends Record> class1;

    private ClientMessageType(Class<? extends Record> class1) {
        this.class1 = class1;
    }

    public Class<? extends Record> getMessageClass() {
        return class1;
    }

}
