package com.email.service.controller;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;


public class EmailSenderActor extends AbstractActor {
    public EmailService emailService;

    public EmailSenderActor(EmailService emailService) {
        this.emailService = emailService;
    }

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(SendEmailRequest.class, this::sendEmail).build();
    }

    public void sendEmail(SendEmailRequest email) {
        SendEmailResponse response = emailService.send(email);
        getSender().tell(response, ActorRef.noSender());
    }
}
