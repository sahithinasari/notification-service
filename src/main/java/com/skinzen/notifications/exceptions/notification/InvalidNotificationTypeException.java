package com.skinzen.notifications.exceptions.notification;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)  // Optional, or use @ControllerAdvice to map
public class InvalidNotificationTypeException extends RuntimeException {
    public InvalidNotificationTypeException(String message) {
        super(message);
    }
}
