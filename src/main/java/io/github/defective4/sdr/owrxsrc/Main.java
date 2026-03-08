package io.github.defective4.sdr.owrxsrc;

import io.github.defective4.sdr.owrxsrc.model.GPS;
import io.github.defective4.sdr.owrxsrc.model.ReceiverDetails;

public class Main {
    public static void main(String[] args) {
        try {
            GPS gps = new GPS(0, 0);
            OWRXConfiguration config = new OWRXConfiguration(2, "default", "none", 8096, "EclipseWaterfall", 3, true,
                    false, "none", gps, new int[] { 32, 48, 80, 145, 2003199, 16777215, 16776960, 16674070, 16711680,
                            12976128, 10420224, 7667712, 4849664 });

            ReceiverDetails details = new ReceiverDetails("OWRX Backend", "", 0, 200, 0, "", "My room", gps, "policy",
                    "AA00");

            new OpenWebRXService(details, config).start(8073);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
