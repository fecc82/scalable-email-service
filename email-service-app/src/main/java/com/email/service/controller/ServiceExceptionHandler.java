package com.email.service.controller;


import com.email.service.data.SendEmailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ServiceExceptionHandler {

    private final String ERROR_MESSAGE = "General Exception Ocurred";

    /**
     * handles any JSON problem and returns http status 400
     *
     * @param ex
     * @return ErrorMessage
     */
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    SendEmailResponse handleWrongJsonFormatException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new SendEmailResponse(HttpStatus.ACCEPTED, errors);
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    SendEmailResponse handleHttpClientErrors(HttpClientErrorException hce) {
        return new SendEmailResponse(hce.getStatusCode(), hce.getStatusText());
    }

    /**
     * handles any unhandled exception for debugging, checking debug message
     *
     * @param ex
     * @return ErrorMessage
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    SendEmailResponse handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return new SendEmailResponse(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
    }
}
