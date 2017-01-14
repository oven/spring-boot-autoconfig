package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConditionalOnClass(ParkingClient.class)
@ConditionalOnProperty("parking.endpoint")
public class ParkingClientAutoConfiguration {
    public ParkingClientAutoConfiguration() {
        System.out.println("\n\n***** Configuring parking client\n\n");
    }

    @Bean
    public ParkingClient parkingClient(@Value("${parking.endpoint}") URI endpoint) {
        ParkingClient parkingClient = new ParkingClient();
        parkingClient.setEndpoint(endpoint);
        return parkingClient;
    }
}