package day.day.up.programming.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class HelloController {

    @Value("${circuit.breaker.open.duration.second:}")
    private String countryList;
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index() {
        System.out.println(countryList);
        return "Greetings from Spring Boot!";
    }

}