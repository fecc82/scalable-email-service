package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl implements EmailService {
    private final static String API_KEY = "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";

    @Override
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        Map<String, Object> parameterValues = mapRequestToApiParams(emailRequest);
        Mail mail = new Mail((Email) parameterValues.get("from"),
                (String) parameterValues.get("subject"),
                (Email) parameterValues.get("to"),
                (Content) parameterValues.get("text"));
        SendGrid sg = new SendGrid(API_KEY);
        SendEmailResponse serviceResponse;
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() == HttpStatus.OK.value() || response.getStatusCode() == HttpStatus.ACCEPTED.value()) {
                serviceResponse = new SendEmailResponse(HttpStatus.OK, SUCCESS);
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return parameterValues;
    }
}