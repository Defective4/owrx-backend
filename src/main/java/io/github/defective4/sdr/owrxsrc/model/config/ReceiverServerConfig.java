package io.github.defective4.sdr.owrxsrc.model.config;

import com.google.gson.annotations.SerializedName;

public record ReceiverServerConfig(@SerializedName("profile_id") String profileId,
        @SerializedName("tuning_step") int tuningStep, @SerializedName("start_freq") int startFrequency,
        @SerializedName("samp_rate") int sampleRate, @SerializedName("center_freq") int centerFrequency,
        @SerializedName("start_mod") String startModulation, @SerializedName("sdr_id") String sdrId,
        @SerializedName("start_offset_freq") int startOffset) {
}
