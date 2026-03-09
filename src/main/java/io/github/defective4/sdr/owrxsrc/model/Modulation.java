package io.github.defective4.sdr.owrxsrc.model;

public enum Modulation {
    NFM("nfm");

    private final String mod;

    private Modulation(String mod) {
        this.mod = mod;
    }

    public String getMod() {
        return mod;
    }
}
