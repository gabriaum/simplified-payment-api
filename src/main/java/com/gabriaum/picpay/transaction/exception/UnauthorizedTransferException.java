package com.gabriaum.picpay.transaction.exception;

public class UnauthorizedTransferException extends RuntimeException {
    public UnauthorizedTransferException(String message) {
        super("Transferência não autorizada: " + message);
    }
}
