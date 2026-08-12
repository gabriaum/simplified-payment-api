package com.gabriaum.picpay.transaction.exception;

public class UserInsufficientBalanceException extends RuntimeException {
    public UserInsufficientBalanceException() {
        super("Saldo insuficiente para realizar a transferência.");
    }
}
