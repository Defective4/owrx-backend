package io.github.defective4.sdr.owrxsrc;

import io.github.defective4.sdr.owrxsrc.model.GPS;
import io.github.defective4.sdr.owrxsrc.model.ReceiverDetails;

public class Main {
    public static void main(String[] args) {
        try {
            ReceiverDetails details = new ReceiverDetails("OWRX Backend", "", 0, 200, 0, "", "My room", new GPS(0, 0),
                    "policy", "AA00");
            new OpenWebRXService(details).start(8073);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
