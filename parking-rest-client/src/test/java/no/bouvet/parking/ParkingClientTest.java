package no.bouvet.parking;

import org.junit.Test;

import java.net.URI;

public class ParkingClientTest {

    @Test
    public void name() throws Exception {
        ParkingClient client = new ParkingClient();
        client.setEndpoint(URI.create("https://www.bergen.kommune.no/wsproxy/parkering.json"));
        CarParkStatus[] result = client.getData();
        for (CarParkStatus status : result) {
            System.out.println(status.getName() + ": " + status.getVacantSpaces() + " [" + status.getTimestamp() + "]");
        }
    }
}
