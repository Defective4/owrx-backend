package io.github.defective4.sdr.owrxsrc.sdr;

import java.util.UUID;

import io.github.defective4.sdr.owrxsrc.model.config.ReceiverServerConfig;

public record ReceiverBand(String name, int tuninStep, int frequency, int sampleRate, int startFrequency,
        int startOffset) {

    public ReceiverServerConfig toConfig(UUID bandId, String modulation, UUID sdrId) {
        return new ReceiverServerConfig(bandId.toString(), tuninStep, startFrequency, sampleRate, frequency, modulation,
                sdrId.toString(), startOffset);
    }
}
