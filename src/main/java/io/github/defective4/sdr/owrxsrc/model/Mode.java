package io.github.defective4.sdr.owrxsrc.model;

import io.github.defective4.sdr.owrxsrc.model.Modulation.Type;

public record Mode(String modulation, String name, Type type, String[] requirements, boolean squelch,
        Bandpass bandpass) {
    public Mode(Modulation modulation) {
        this(modulation.getMod(), modulation.getName(), modulation.getType(), new String[0], modulation.isSquelch(),
                modulation.getBandpass());
    }
}
