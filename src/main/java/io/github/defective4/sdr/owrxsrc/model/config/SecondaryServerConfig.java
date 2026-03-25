package io.github.defective4.sdr.owrxsrc.model.config;

import com.google.gson.annotations.SerializedName;

public record SecondaryServerConfig(@SerializedName("secondary_fft_size") int secondaryFFT) {

}
