package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

public class ParkingClientHealthIndicator extends AbstractHealthIndicator {
    
    private  ParkingClient parkingClient;

    public ParkingClientHealthIndicator(ParkingClient parkingClient) {
        this.parkingClient = parkingClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            parkingClient.getData();
            builder.up();
            
        } catch (Exception e) {
            builder.down();
        }
    }
}
