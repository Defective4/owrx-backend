package io.github.defective4.sdr.owrxsrc.model;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public record StartingParams(@SerializedName("start_mod") String startingModulation,
        @SerializedName("profile_id") String profileId, @SerializedName("center_freq") int centerFrequency,
        @SerializedName("start_freq") int startingFrequency, @SerializedName("samp_rate") int sampleRate,
        @SerializedName("tuning_step") int tuningStepHz, @SerializedName("sdr_id") String sdrId,
        @SerializedName("start_offset_freq") int startingOffset) {
    public StartingParams(Modulation startingModulation, UUID profileId, int centerFrequency, int startingFrequency,
            int sampleRate, int tuningStepHz, UUID sdrId, int startingOffset) {
        this(startingModulation.getMod(), profileId.toString(), centerFrequency, startingFrequency, sampleRate,
                tuningStepHz, sdrId.toString(), startingOffset);
    }
}
