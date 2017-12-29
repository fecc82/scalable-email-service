package com.email.service;

import org.springframework.http.HttpStatus;

public class EmailServiceResponse {
    private HttpStatus status;
    private String data;
    private boolean errorOccured;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }
}
