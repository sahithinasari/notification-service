package com.skinzen.notifications.dto;

import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private Long userId;
    private String messageType; // e.g., WELCOME, PASSWORD_RESET
    private Map<String, Object> data; // dynamic content (e.g., username, OTP)

}
