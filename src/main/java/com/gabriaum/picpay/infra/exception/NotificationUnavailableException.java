package com.gabriaum.picpay.infra.exception;

public class NotificationUnavailableException extends RuntimeException {
    public NotificationUnavailableException(String message) {
        super(message);
    }
}
