package io.github.defective4.sdr.owrxsrc;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.github.defective4.sdr.owrxsrc.model.client.message.ClientChatMessage;
import io.github.defective4.sdr.owrxsrc.session.ClientSession;

public class MessageHandler {

    private final OpenWebRXService service;

    public MessageHandler(OpenWebRXService service) {
        this.service = service;
    }

    public void handleMessage(ClientSession client, Record message) {
        for (Method method : getClass().getDeclaredMethods()) if (method.isAnnotationPresent(MessageReceiver.class)) {
            Parameter[] params = method.getParameters();
            if (params.length == 2 && params[0].getType() == client.getClass()
                    && params[1].getType() == message.getClass())
                try {
                    method.invoke(this, client, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
        }
    }

    @MessageReceiver
    protected void handleClientChat(ClientSession client, ClientChatMessage msg) {
        service.broadcastChatMessage(msg.name(), msg.text(), Color.white);
    }
}
