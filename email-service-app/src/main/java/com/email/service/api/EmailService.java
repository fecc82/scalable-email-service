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
     * Map SendEmailRequest to a map. This to cater for different requirements needed for each API.
     * @param emailRequest
     * @return
     */
    Map mapRequestToApiParams(SendEmailRequest emailRequest);
}
