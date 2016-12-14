package no.bouvet.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping
    public String hello() {
        return "hello, world!";
    }
}
