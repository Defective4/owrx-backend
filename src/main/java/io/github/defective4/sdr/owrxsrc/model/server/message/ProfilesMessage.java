package io.github.defective4.sdr.owrxsrc.model.server.message;

import java.util.List;

import io.github.defective4.sdr.owrxsrc.model.ReceiverProfile;

public class ProfilesMessage extends ServerMessage {

    private final List<ReceiverProfile> value;

    public ProfilesMessage(List<ReceiverProfile> value) {
        super("profiles");
        this.value = value;
    }

}
