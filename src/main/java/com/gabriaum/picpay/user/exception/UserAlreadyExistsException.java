package com.gabriaum.picpay.user.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Usuário já existe com o mesmo CPF ou E-mail.");
    }
}