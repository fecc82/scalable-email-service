package com.email.service.impl;

import com.email.service.api.EmailService;
import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * This is for testing application locally in cucumber and so it doesnt spam emails.
 */
@Service
@Qualifier("local")
public class LocalTestServiceImpl implements EmailService {
    public SendEmailResponse send(SendEmailRequest emailRequest) {
        return new SendEmailResponse(HttpStatus.OK, SUCCESS);
    }

    public Map mapRequestToApiParams(SendEmailRequest emailRequest) {
        return null;
    }
}
