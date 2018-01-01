package com.email.service.cucumber;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Object for Sending Email Response
 */
public class EmailStepsResponse implements Serializable {
    private HttpStatus status;
    private String message;
    private String timeStamp;

    public EmailStepsResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        timeStamp = LocalDateTime.now().toString();
    }

    public EmailStepsResponse() {
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
