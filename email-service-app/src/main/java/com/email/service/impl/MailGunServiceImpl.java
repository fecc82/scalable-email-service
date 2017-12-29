package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
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
import java.util.HashMap;
import java.util.Map;
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
        Map<String, String> parameterValues = mapRequestToApiParams(emailRequest);
        return sendPostRequest(URL, parameterValues);
    }

    private SendEmailResponse sendPostRequest(String uri, Map<String, String> parameters) {
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
            serviceResponse = new SendEmailResponse(response.getStatusCode(), SUCCESS);
        } catch (Exception ex) {
            serviceResponse = new SendEmailResponse(HttpStatus.NOT_FOUND, ERROR);
            ex.printStackTrace();
        }
        return serviceResponse;
    }

    public Map<String, String> mapRequestToApiParams(SendEmailRequest emailRequest) {
        Map<String, String> parameterValues = new HashMap<>();
        parameterValues.put("to", emailRequest.getRecipients().stream().collect(Collectors.joining(",")));
        parameterValues.put("from", emailRequest.getSender());
        parameterValues.put("subject", emailRequest.getHtmlTitle());
        parameterValues.put("text", emailRequest.getHtmlBody());
        return parameterValues;
    }
}
