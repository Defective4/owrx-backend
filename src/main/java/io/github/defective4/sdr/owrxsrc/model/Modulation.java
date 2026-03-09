package io.github.defective4.sdr.owrxsrc.model;

public enum Modulation {
    AM("am", "AM", Type.analog, true, new Bandpass(-4000, 4000)),
    NFM("nfm", "FM", Type.analog, true, new Bandpass(-4000, 4000)),
    RAW("raw", "RAW", Type.analog, true, new Bandpass(-24000, 24000)),
    WFM("wfm", "WFM", Type.analog, true, new Bandpass(-75000, 75000));

    public static enum Type {
        analog;
    }

    private final Bandpass bandpass;
    private final String mod;
    private final String name;
    private final boolean squelch;
    private final Type type;

    private Modulation(String mod, String name, Type type, boolean squelch, Bandpass bandpass) {
        this.mod = mod;
        this.name = name;
        this.type = type;
        this.squelch = squelch;
        this.bandpass = bandpass;
    }

    public Bandpass getBandpass() {
        return bandpass;
    }

    public String getMod() {
        return mod;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isSquelch() {
        return squelch;
    }
}
