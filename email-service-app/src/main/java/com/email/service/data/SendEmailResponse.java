package com.email.service.data;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;


public class SendEmailResponse implements Serializable {
    private HttpStatus status;
    private String message;
    private String timeStamp;

    public SendEmailResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        timeStamp = LocalDateTime.now().toString();
    }

    public SendEmailResponse() {
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
