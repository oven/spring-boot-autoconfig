package no.bouvet.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

/*
    @Bean
    public ParkingClient parkingClient() {
        ParkingClient parkingClient = new ParkingClient();
        parkingClient.setEndpoint(URI.create("http://www.vg.no"));
        return parkingClient;
    }
*/

}
