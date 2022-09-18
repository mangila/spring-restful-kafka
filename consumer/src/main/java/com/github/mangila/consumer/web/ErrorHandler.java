package com.github.mangila.consumer.web;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleDataAccessResourceFailureException(DataAccessResourceFailureException exception,
                                                                           WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
