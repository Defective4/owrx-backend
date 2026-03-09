package io.github.defective4.sdr.owrxsrc.model.server.message;

import io.github.defective4.sdr.owrxsrc.model.Features;

public class FeaturesMessage extends ServerMessage {
    private final Features value;

    public FeaturesMessage(Features value) {
        super("features");
        this.value = value;
    }
}
