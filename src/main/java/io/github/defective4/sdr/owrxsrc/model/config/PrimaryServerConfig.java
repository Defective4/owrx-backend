package io.github.defective4.sdr.owrxsrc.model.config;

import com.google.gson.annotations.SerializedName;

import io.github.defective4.sdr.owrxsrc.OWRXConfiguration;
import io.github.defective4.sdr.owrxsrc.model.GPS;
import io.github.defective4.sdr.owrxsrc.model.Levels;

public record PrimaryServerConfig(@SerializedName("max_clients") int maxClients,
        @SerializedName("allow_audio_recording") boolean allowRecording, @SerializedName("vessel_url") String vesselURL,
        @SerializedName("callsign_url") String callsignURL,
        @SerializedName("waterfall_auto_min_range") int waterfallAutoMinRange,
        @SerializedName("waterfall_auto_levels") Levels waterfallAutoLevels, @SerializedName("ui_theme") String theme,
        @SerializedName("fft_compression") String fftCompression, @SerializedName("fft_size") int fftSize,
        @SerializedName("waterfall_scheme") String waterfallTheme,
        @SerializedName("tuning_precision") int tuningPrecision, @SerializedName("allow_chat") boolean allowChat,
        @SerializedName("flight_url") String flightURL,
        @SerializedName("allow_center_freq_changes") boolean allowCenterFrequencyChanges,
        @SerializedName("modes_url") String modesURL, @SerializedName("audio_compression") String audioCompression,
        @SerializedName("receiver_gps") GPS gps, @SerializedName("waterfall_colors") int[] waterfallColors,
        @SerializedName("waterfall_levels") Levels waterfallLevels,
        @SerializedName("squelch_auto_margin") int squelchAutoMargin,
        @SerializedName("waterfall_auto_level_default_mode") boolean waterfallAutoLevelsMode) {

    public PrimaryServerConfig(OWRXConfiguration config) {
        this(config.maxClients(), false, "https://www.vesselfinder.com/vessels/details/{}",
                "https://www.qrz.com/call/{}", 50, new Levels(3, 10), config.theme(), config.fftCompression(),
                config.fftSize(), config.waterfallTheme(), config.tuningPrecision(), config.allowChat(),
                "https://flightaware.com/live/flight/{}", config.allowCenterFrequencyChanges(),
                "https://flightaware.com/live/modes/{}/redirect", config.audioCompression(), config.gps(),
                config.waterfallColors(), config.waterfallLevels(), 10, false);
    }
}
