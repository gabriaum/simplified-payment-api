package com.gabriaum.picpay.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InfraExceptionHandler {
    @ExceptionHandler(NotificationUnavailableException.class)
    public ResponseEntity<?> handleNotificationUnavailableException(NotificationUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }
}