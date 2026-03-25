package io.github.defective4.sdr.owrxsrc.model;

import java.util.UUID;

public record ReceiverProfile(String combinedID, String name) {
    public ReceiverProfile(UUID sdrID, UUID profileID, String name) {
        this(sdrID + "|" + profileID, name);
    }
}
