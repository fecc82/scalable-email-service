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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl implements EmailService {
    private final static String SENDGRID_API_KEY =System.getenv("SENDGRID_API_KEY");

    @Override
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        AppEvents.eventOf("Checking if Send Grid API is valid, KEY_SIZE: "
                + Optional.ofNullable(SENDGRID_API_KEY).orElse("").length(), emailRequest);
        AppEvents.eventOf("Trying Send Grid send Email", emailRequest);
        Map<String, Object> parameterValues = mapRequestToApiParams(emailRequest);
        Mail mail = new Mail((Email) parameterValues.get("from"),
                (String) parameterValues.get("subject"),
                (Email) parameterValues.get("to"),
                (Content) parameterValues.get("text"));
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
        parameterValues.put("to", new Email(emailRequest.getRecipients().stream().collect(Collectors.joining(","))));
        parameterValues.put("from", new Email(emailRequest.getSender()));
        parameterValues.put("subject", emailRequest.getHtmlTitle());
        parameterValues.put("text", new Content("text/plain", emailRequest.getHtmlBody()));
        parameterValues.put("cc", Optional.ofNullable(emailRequest.getCc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues.put("bcc", Optional.ofNullable(emailRequest.getBcc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues = parameterValues.entrySet().stream()
                .filter(entry -> (entry.getValue() != null && !"".equals(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return parameterValues;
    }
}