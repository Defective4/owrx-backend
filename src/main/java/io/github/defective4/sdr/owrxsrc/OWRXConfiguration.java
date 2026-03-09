package io.github.defective4.sdr.owrxsrc;

import io.github.defective4.sdr.owrxsrc.model.GPS;
import io.github.defective4.sdr.owrxsrc.model.Levels;

public record OWRXConfiguration(int maxClients,
//         boolean allowRecording,
//         String vesselURL,
//         String callsignURL,
//         int waterfallAutoMinRange,
//         Levels waterfallAutoLevels,
        String theme, String fftCompression, int fftSize, String waterfallTheme, int tuningPrecision, boolean allowChat,
//         String flightURL,
        boolean allowCenterFrequencyChanges,
//         String modesURL,
        String audioCompression, GPS gps, int[] waterfallColors,
        Levels waterfallLevels) {

}
