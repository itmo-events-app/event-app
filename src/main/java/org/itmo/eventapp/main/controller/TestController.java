package org.itmo.eventapp.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("hello")
    String sayHello(@RequestParam String s) {
        return "Hello, " + s + "!";
    }
}
