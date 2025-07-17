package com.skinzen.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private String status; // SUCCESS, FAILURE
    private LocalDateTime timestamp;
    private String messageId;

    // Getters, Setters, Constructor(s)
}
