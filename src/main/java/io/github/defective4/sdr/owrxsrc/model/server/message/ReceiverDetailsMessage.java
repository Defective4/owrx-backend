package io.github.defective4.sdr.owrxsrc.model.server.message;

import io.github.defective4.sdr.owrxsrc.model.ReceiverDetails;

public class ReceiverDetailsMessage extends ServerMessage {

    private final ReceiverDetails value;

    public ReceiverDetailsMessage(ReceiverDetails value) {
        super("receiver_details");
        this.value = value;
    }

}
