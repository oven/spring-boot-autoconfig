package no.bouvet.autoconfig;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingClientAutoConfiguration {
    public ParkingClientAutoConfiguration() {
        System.out.println("\n\n***** Configuring parking client\n\n");
    }
}