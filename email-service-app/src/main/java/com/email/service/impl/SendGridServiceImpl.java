package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl implements EmailService {
    private final static String API_KEY = System.getenv("SEND-GRID-API-KEY");

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
        parameterValues.put("cc", Optional.ofNullable(emailRequest.getCc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues.put("bcc", Optional.ofNullable(emailRequest.getBcc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues = parameterValues.entrySet().stream()
                .filter(entry -> (entry.getValue() != null && !"".equals(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return parameterValues;
    }
}