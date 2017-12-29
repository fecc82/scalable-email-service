package com.email.service.api;

import com.email.service.data.SendEmailRequest;
import com.email.service.data.SendEmailResponse;

import java.util.Map;

public interface EmailService {
    String SUCCESS = "Email has been sent";
    String ERROR = "Error Occurred during sending of Email";
    /**
     * Actual implementation of sending of email
     * @param emailRequest
     * @return SendEmailResponse
     */
    SendEmailResponse send(SendEmailRequest emailRequest);

    /**
     * map request to
     * @param emailRequest
     * @return
     */
    Map mapRequestToApiParams(SendEmailRequest emailRequest);
}
