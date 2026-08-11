package com.gabriaum.picpay.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Usuário não encontrado ou as credenciais estão incorretas.");
    }
}