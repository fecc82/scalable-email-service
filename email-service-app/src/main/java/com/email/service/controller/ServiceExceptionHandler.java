package com.email.service.controller;


import com.email.service.data.SendEmailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

/**
 * Exception Handler for API Service
 */
@ControllerAdvice
public class ServiceExceptionHandler {

    private final String ERROR_MESSAGE = "General Exception Occurred";
    private final String METHOD_NOT_ALLOWED = "Method not allowed";
    private final String API_CALL_EMPTY = "Request Parameters can't be empty";
    private final String AKKA_TIMEOUT = "Unable to Send Email, none of the Third Party Email APIs returned a Success Message";

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
        return new SendEmailResponse(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    SendEmailResponse handleGeneralException(HttpRequestMethodNotSupportedException ex) {
        return new SendEmailResponse(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED);
    }


    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    SendEmailResponse handleEmpty(HttpMessageNotReadableException ex) {
        return new SendEmailResponse(HttpStatus.METHOD_NOT_ALLOWED, API_CALL_EMPTY);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({AsyncRequestTimeoutException.class,CompletionException.class})
    SendEmailResponse handleAPITimeOutException(Exception ex) {
        return new SendEmailResponse(HttpStatus.BAD_REQUEST, AKKA_TIMEOUT);
    }


}
