package io.github.defective4.sdr.owrxsrc.model.server.message;

public class ServerChatMessage extends ServerMessage {

    private final String name, text, color;

    public ServerChatMessage(String name, String text, String color) {
        super("chat_message");
        this.name = name;
        this.text = text;
        this.color = color;
    }

}
