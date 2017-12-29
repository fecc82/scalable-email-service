package com.email.service.main;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@SpringBootApplication(scanBasePackages = {"com.email.service.*"})
public class MainApplication {

    @Autowired
    @Qualifier("mailGun")
    private EmailService emailSenderService;

    @RequestMapping(value = "/health")
    public CompletableFuture<String> health() {
        return CompletableFuture.completedFuture("OK!");
    }

    @RequestMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public CompletableFuture<SendEmailResponse> postEmail(@Valid @RequestBody SendEmailRequest email) {
        return CompletableFuture.completedFuture(emailSenderService.send(email));
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }


}
