package com.email.service.controller;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.email.service.util.AppEvents;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashSet;


/**
 * Akka Actor for handling the actual sending of email. non-blocking.
 */
public class EmailSenderActor extends AbstractActor {
    public LinkedHashSet<EmailService> emailServiceSet;

    public EmailSenderActor(LinkedHashSet<EmailService> emailServiceSet) {
        this.emailServiceSet = emailServiceSet;
    }

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(SendEmailRequest.class, this::sendEmail).build();
    }

    public void sendEmail(SendEmailRequest request) {
        AppEvents.eventOf("Email Actor has Received the Request for processing", request);
        for (EmailService service : emailServiceSet) {
            SendEmailResponse response = service.send(request);
            if (response.getStatus() == HttpStatus.OK || response.getStatus() == HttpStatus.ACCEPTED) {
                getSender().tell(response, ActorRef.noSender());
                break;
            }
        }
    }
}
