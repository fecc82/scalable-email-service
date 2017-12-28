package com.email.service.impl;

import com.email.service.api.EmailService;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl implements EmailService{
    private final static String API_KEY= "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";
    public void send(String to2, String cc, String email) {
        Email from = new Email("meltatlonghari3@gmail.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("meltatlonghari3@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
