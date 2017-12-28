package com.email.service.main;

import com.email.service.api.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(scanBasePackages = {"com.email.service.impl"})
@RestController
public class MainApplication {

    @Autowired
    @Qualifier("mailGun")
    private EmailService email;

    @RequestMapping("/")
    public String home() {
        return "Hello World2";
    }

    @RequestMapping("/test")
    public String test() {
        email.send(null, null, null);
        return "Hello World";
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }


}
