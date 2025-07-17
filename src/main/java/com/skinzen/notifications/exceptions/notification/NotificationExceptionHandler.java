package com.skinzen.notifications.exceptions.notification;

import com.skinzen.notifications.exceptions.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Slf4j
public class NotificationExceptionHandler {

    public static ApiError handle(Exception ex) {
        return handle(ex, null); // path is optional for non-HTTP
    }

    public static ApiError handle(Exception ex, String path) {
        HttpStatus status = resolveHttpStatus(ex);
        log.error("Handled exception: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

        return ApiError.builder()
                .status(status.value())
                .message("Internal error occurred")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    private static HttpStatus resolveHttpStatus(Exception ex) {
        if (ex instanceof IllegalArgumentException || ex instanceof InvalidNotificationTypeException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
