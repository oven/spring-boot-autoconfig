package no.bouvet.webapp;

import no.bouvet.parking.CarParkStatus;
import no.bouvet.parking.ParkingClient;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MyController {
    @Autowired
    ParkingClient parkingClient;

    @GetMapping
    public String hello() {
        return "hello, world!";
    }

    @GetMapping("/now")
    public LocalDate now() {
        return new LocalDate();
    }

    @GetMapping("/parking")
    public CarParkStatus[] parking() throws IOException {
        return parkingClient.getData();
    }
}
