package com.gabriaum.picpay.transaction.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(UserInsufficientBalanceException.class)
    public ResponseEntity<String> handleUserInsufficientBalanceException(UserInsufficientBalanceException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedTransferException.class)
    public ResponseEntity<String> handleUnauthorizedTransferException(UnauthorizedTransferException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }
}
