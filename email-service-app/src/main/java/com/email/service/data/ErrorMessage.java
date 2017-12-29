package com.email.service.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorMessage {
    private HttpStatus status;
    private List<String> message;
    @JsonIgnore
    private String debugMessage;
    private String timeStamp;

    public void generateErrorMessage(HttpStatus status, List<String> message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
        timeStamp = LocalDateTime.now().toString();
    }
    public void generateErrorMessage(HttpStatus status, String message, String debugMessage) {
        ArrayList<String> messages = new ArrayList<String>(Arrays.asList(message));
        generateErrorMessage(status, messages, debugMessage);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}
