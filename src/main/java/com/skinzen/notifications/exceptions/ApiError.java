package com.skinzen.notifications.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private int status;
    private String message;
    private String details;
    private String path; // optional if no URI involved
    private LocalDateTime timestamp;
}
