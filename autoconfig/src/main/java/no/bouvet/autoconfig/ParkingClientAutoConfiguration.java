package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class ParkingClientAutoConfiguration {
    public ParkingClientAutoConfiguration() {
        System.out.println("\n\n***** Configuring parking client\n\n");
    }

    @Bean
    public ParkingClient parkingClient() {
        ParkingClient parkingClient = new ParkingClient();
        parkingClient.setEndpoint(URI.create("https://www.bergen.kommune.no/wsproxy/parkering.json"));
        return parkingClient;
    }
}