package io.github.defective4.sdr.owrxsrc.model;

public enum Modulation {
    NFM("nfm");

    private final String mod;

    public String getMod() {
        return mod;
    }

    private Modulation(String mod) {
        this.mod = mod;
    }
}
