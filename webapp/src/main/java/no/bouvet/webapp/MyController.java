package no.bouvet.webapp;

import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping
    public String hello() {
        return "hello, world!";
    }

    @GetMapping("/now")
    public LocalDate now() {
        return new LocalDate();
    }
}
