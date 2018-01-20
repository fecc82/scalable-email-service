package com.email.service.main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import com.email.service.api.EmailService;
import com.email.service.controller.EmailSenderActor;
import com.email.service.controller.SimpleClusterListener;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.email.service.util.AppEvents;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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

/**
 * Main Spring Boot Application
 */
@RestController
@SpringBootApplication(scanBasePackages = {"com.email.service.*"})
public class MainApplication {

    private static final String EMAIL_ACTOR = "email_actor";
    private final static int TIME_OUT = 15000;
    private String AKKA_PORT = System.getenv("AKKA_PORT");
    @Autowired @Qualifier("local") private EmailService localMailService;
    @Autowired @Qualifier("mailGun") private EmailService mailGunMailService;
    @Autowired @Qualifier("sendGrid") private EmailService sendGridMailService;

    private ActorSystem actorSystem;
    private ActorRef actorRef;
    public MainApplication(){
        if (null == AKKA_PORT) {
            AKKA_PORT = "2551";
        }
        //  actorSystem = ActorSystem.create();
        //   String[] ports = new String[] { "2551", "2552", "0" };
        //  for (String port : ports) {
        // Override the configuration of the port
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + AKKA_PORT + "\n" +
                "akka.remote.artery.canonical.port=" + AKKA_PORT)
                .withFallback(ConfigFactory.load());
        // Create an Akka system
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        // Create an actor that handles cluster domain events
        system.actorOf(Props.create(SimpleClusterListener.class), "clusterListener");
        // }
    }

    @RequestMapping(value = "/")
    public String health() {
        AppEvents.eventOf("API Health Check",null);
        return "App OK! Akka cluster port is : " + AKKA_PORT;
    }

    @RequestMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public CompletionStage<SendEmailResponse> postEmail(@Valid @RequestBody SendEmailRequest email) {
        AppEvents.eventOf("Received Call API Request",  email);
        if(null == actorRef) {
            LinkedHashSet<EmailService> services = new LinkedHashSet<>(Arrays.asList(localMailService, mailGunMailService, sendGridMailService));
            actorRef = actorSystem.actorOf(Props.create(EmailSenderActor.class, services), EMAIL_ACTOR);
        }
        return PatternsCS.ask(actorRef, email,TIME_OUT).thenComposeAsync(result-> CompletableFuture.completedFuture((SendEmailResponse)result));
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
