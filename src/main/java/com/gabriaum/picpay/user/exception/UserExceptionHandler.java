package com.gabriaum.picpay.user.exception;

import com.gabriaum.picpay.user.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Map;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404)
                .body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(409)
                .body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException && invalidFormatException.getTargetType() == Role.class)
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message", "Tipo de conta inválido (Role)",
                            "allowed", Role.values()
                    )
            );

        return ResponseEntity.badRequest().body(
                Map.of("message", "Corpo da requisição inválido")
        );
    }
}