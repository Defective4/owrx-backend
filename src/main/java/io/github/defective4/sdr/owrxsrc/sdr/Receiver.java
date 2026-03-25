package io.github.defective4.sdr.owrxsrc.sdr;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class Receiver {
    private final Map<UUID, ReceiverBand> bands;
    private final String name;

    public Receiver(String name, Map<UUID, ReceiverBand> bands) {
        this.bands = bands;
        this.name = name;
    }

    public Map<UUID, ReceiverBand> getBands() {
        return Collections.unmodifiableMap(bands);
    }

    public String getName() {
        return name;
    }
}
