package com.gabriel.empregos.api.jobs.common.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabriel.empregos.api.jobs.common.dtos.ErrorResponse;
import com.gabriel.empregos.core.exceptions.ModelNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .build();
    }

}
