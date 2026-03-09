package io.github.defective4.sdr.owrxsrc.model.server.message;

import io.github.defective4.sdr.owrxsrc.model.Mode;

public class ModesMessage extends ServerMessage {
    private final Mode[] value;

    public ModesMessage(Mode[] value) {
        super("modes");
        this.value = value;
    }
}
