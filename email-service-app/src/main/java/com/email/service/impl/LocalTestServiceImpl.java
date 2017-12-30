package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import com.email.service.util.AppEvents;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * This is for testing application locally in cucumber and so it doesnt spam emails.
 * And for testing failovers
 */
@Service
@Qualifier("local")
public class LocalTestServiceImpl implements EmailService {
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        AppEvents.eventOf("Trying Local Service for failover", emailRequest);
        return new SendEmailResponse(HttpStatus.NOT_FOUND, ERROR);
    }

    public Map mapRequestToApiParams(SendEmailRequest emailRequest) {
        return null;
    }
}
