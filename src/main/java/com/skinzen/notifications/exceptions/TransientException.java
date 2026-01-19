package com.skinzen.notifications.exceptions;

public class TransientException extends RuntimeException {
    public TransientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}


