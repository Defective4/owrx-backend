package io.github.defective4.sdr.owrxsrc.sdr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.defective4.sdr.owrxsrc.model.ReceiverProfile;

public class Receivers {
    private final Map<UUID, Receiver> receivers = new LinkedHashMap<>();
    private final UUID selectedBand;
    private final UUID selectedReceiver;

    public Receivers() {
        ReceiverBand band = new ReceiverBand("Test band", 1000, 100000000, 2400000, 100000000, 0);
        receivers.put(UUID.randomUUID(),
                new Receiver("Test receiver", Map.of(UUID.randomUUID(), band, UUID.randomUUID(), band)));
        selectedReceiver = receivers.keySet().iterator().next();
        selectedBand = receivers.values().iterator().next().getBands().keySet().iterator().next();
    }

    public List<ReceiverProfile> getProfiles() {
        List<ReceiverProfile> profiles = new ArrayList<>();
        receivers.forEach((rid, rx) -> rx.getBands()
                .forEach((bid, band) -> profiles.add(new ReceiverProfile(rid, bid, rx.getName() + " " + band.name()))));
        return Collections.unmodifiableList(profiles);
    }



    public Map<UUID, Receiver> getReceivers() {
        return receivers;
    }



    public UUID getSelectedBand() {
        return selectedBand;
    }



    public UUID getSelectedReceiver() {
        return selectedReceiver;
    }
}
