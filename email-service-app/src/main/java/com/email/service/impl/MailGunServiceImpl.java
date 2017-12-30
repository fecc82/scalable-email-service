package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.email.service.util.AppEvents;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("mailGun")
public class MailGunServiceImpl implements EmailService {
    private final String API_KEY = "";
    private final String URL = "https://api.mailgun.net/v3/sandbox9073ddbcd8d14680ad6289ef9ca73ed2.mailgun.org/messages";
    private final RestTemplate restTemplate;

    public MailGunServiceImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        AppEvents.eventOf("Trying to FETCH API in Environment key " + System.getenv("API_KEY"), emailRequest);
        AppEvents.eventOf("Trying Mail Gun to send Email", emailRequest);
        Map<String, String> parameterValues = mapRequestToApiParams(emailRequest);
        return sendPostRequest(URL, parameterValues,emailRequest);
    }

    private SendEmailResponse sendPostRequest(String uri, Map<String, String> parameters, SendEmailRequest emailRequest) {
        HttpHeaders headers = new HttpHeaders() {{
            final String encodeKey = "api:" + API_KEY;
            byte[] encodedAuth = Base64.encodeBase64(encodeKey.getBytes(Charset.forName("US-ASCII")));
            final String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.setAll(parameters);
        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, headers);
        SendEmailResponse serviceResponse;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
            AppEvents.eventOf("Send Mail Gun Email Success", emailRequest);
            serviceResponse = new SendEmailResponse(response.getStatusCode(), SUCCESS);
        } catch (Exception ex) {
            AppEvents.eventOf("Send Mail Gun Email Failure", emailRequest, ex);
            serviceResponse = new SendEmailResponse(HttpStatus.NOT_FOUND, ERROR);
            ex.printStackTrace();
        }
        return serviceResponse;
    }

    public Map<String, String> mapRequestToApiParams(SendEmailRequest emailRequest) {
        Map<String, String> parameterValues = new HashMap<>();
        parameterValues.put("to", emailRequest.getRecipients().stream().collect(Collectors.joining(",")));
        parameterValues.put("from", emailRequest.getSender());
        parameterValues.put("cc", Optional.ofNullable(emailRequest.getCc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues.put("bcc", Optional.ofNullable(emailRequest.getBcc()).orElse(new ArrayList<>()).stream().collect(Collectors.joining(",")));
        parameterValues.put("subject", emailRequest.getHtmlTitle());
        parameterValues.put("text", emailRequest.getHtmlBody());
        parameterValues = parameterValues.entrySet().stream()
                .filter(entry -> (entry.getValue() != null && !"".equals(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return parameterValues;
    }
}
