package com.email.service.main;

import com.email.service.api.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@SpringBootApplication(scanBasePackages = {"com.email.service.impl"})
@RestController
public class MainApplication {

    @Autowired
    @Qualifier("mailGun")
    private EmailService email;

    @RequestMapping("/sendEmail")
    public CompletableFuture<String> test(){
        email.send(null,null,null);
        return  CompletableFuture.completedFuture("OK!");
    }
    @RequestMapping("/")
    public CompletableFuture<String> health(){
        return  CompletableFuture.completedFuture("OK!");
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }


}
