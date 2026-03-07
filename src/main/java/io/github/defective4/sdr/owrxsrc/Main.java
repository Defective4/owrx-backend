package io.github.defective4.sdr.owrxsrc;

import io.github.defective4.sdr.owrxsrc.model.ServiceDetails;

public class Main {
    public static void main(String[] args) {
        try {
            ServiceDetails details = new ServiceDetails("/", "OWRX Backend Service", "My room", "aa00", 200, "-osm", 0, null, "An example photo", "An example description");
            new OpenWebRXService(details).start(8073);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
