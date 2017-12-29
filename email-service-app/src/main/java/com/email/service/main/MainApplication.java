package com.email.service.main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import com.email.service.api.EmailService;
import com.email.service.controller.EmailSenderActor;
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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@SpringBootApplication(scanBasePackages = {"com.email.service.*"})
public class MainApplication {

    private static final String EMAIL_ACTOR = "email_actor";
    private final static int TIME_OUT = 15000;

    @Autowired @Qualifier("local") private EmailService localMailService;
    @Autowired @Qualifier("mailGun") private EmailService mailGunMailService;
    @Autowired @Qualifier("sendGrid") private EmailService sendGridMailService;

    private ActorSystem actorSystem;

    public MainApplication(){
        actorSystem = ActorSystem.create();
    }

    @RequestMapping(value = "/health")
    public CompletionStage<String> health() {
        return CompletableFuture.completedFuture("OK!");
    }

    @RequestMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public CompletionStage<SendEmailResponse> postEmail(@Valid @RequestBody SendEmailRequest email) {
        LinkedHashSet<EmailService> services = new LinkedHashSet<>(Arrays.asList(localMailService, mailGunMailService,sendGridMailService));
        ActorRef r = actorSystem.actorOf(Props.create(EmailSenderActor.class,services), EMAIL_ACTOR);
        return PatternsCS.ask(r, email,TIME_OUT).thenComposeAsync(result-> CompletableFuture.completedFuture((SendEmailResponse)result));
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
