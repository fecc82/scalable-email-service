package com.email.service.controller;

import com.email.service.data.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
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
    @ResponseStatus
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorMessage handleWrongJsonFormatException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.generateErrorMessage(HttpStatus.BAD_REQUEST, errors, ex.getMessage());
        return errorMessage;
    }

    /**
     * handles any unhandled exception for debugging, checking debug message
     *
     * @param ex
     * @return ErrorMessage
     */
    @ResponseStatus
    @ResponseBody
    @ExceptionHandler(Exception.class)
    ErrorMessage handleGeneralException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        if (ex instanceof HttpClientErrorException) {
            HttpClientErrorException hce = (HttpClientErrorException) ex;
            errorMessage.generateErrorMessage(hce.getStatusCode(), hce.getStatusText(), ex.getMessage());
        } else {
            errorMessage.generateErrorMessage(HttpStatus.NOT_FOUND, ERROR_MESSAGE, ex.getMessage());
            ex.printStackTrace();
        }
        return errorMessage;
    }
}
