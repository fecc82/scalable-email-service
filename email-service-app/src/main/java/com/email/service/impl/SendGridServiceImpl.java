package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.email.service.util.AppEvents;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Integration with Send Grid Email Service
 */
@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl implements EmailService {
    private final static String SENDGRID_API_KEY =System.getenv("SENDGRID_API_KEY");

    @Override
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        AppEvents.eventOf("Checking if Send Grid API is valid, KEY_SIZE: " + Optional.ofNullable(SENDGRID_API_KEY).orElse("").length(), emailRequest);
        AppEvents.eventOf("Trying Send Grid send Email", emailRequest);
        Map<String, Object> parameterValues = mapRequestToApiParams(emailRequest);
        Mail mail = (Mail)parameterValues.get("mail");
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        SendEmailResponse serviceResponse;
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() == HttpStatus.OK.value() || response.getStatusCode() == HttpStatus.ACCEPTED.value()) {
                serviceResponse = new SendEmailResponse(HttpStatus.OK, SUCCESS);
                AppEvents.eventOf("Send Grid Email Success", emailRequest);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
        } catch (HttpClientErrorException hce) {
            AppEvents.eventOf("Send Grid Email API Failure", emailRequest, hce);
            serviceResponse = new SendEmailResponse(HttpStatus.BAD_REQUEST, ERROR);
        } catch (Exception ex) {
            AppEvents.eventOf("General Error: Send Grid Email Failure", emailRequest, ex);
            serviceResponse = new SendEmailResponse(HttpStatus.NOT_FOUND, ERROR);
        }
        return serviceResponse;
    }

    public Map<String, Object> mapRequestToApiParams(SendEmailRequest emailRequest) {
        Map<String, Object> parameterValues = new HashMap<>();
        Mail mail = new Mail();
        Personalization p = new Personalization();
        emailRequest.getRecipients().forEach(email -> p.addTo(new Email(email)));
        emailRequest.getCc().forEach(cc -> p.addCc(new Email(cc)));
        emailRequest.getBcc().forEach(bcc-> p.addBcc(new Email(bcc)));
        mail.setSubject(emailRequest.getHtmlTitle());
        mail.addContent(new Content("text/plain", emailRequest.getHtmlBody()));
        mail.addPersonalization(p);
        mail.setFrom(new Email(emailRequest.getSender()));
        parameterValues.put("mail", mail);
        return parameterValues;
    }
}