package com.skinzen.notifications.dto;

import com.skinzen.notifications.enums.NotificationType;
import lombok.Data;

import java.util.Map;

@Data
public class Notification {
    private String recipient;                 // email/phone/deviceToken etc.
    private NotificationType type;            // Resolved from messageType
    private String template;                  // Resolved from type or db
    private Map<String, Object> data;         // Dynamic values
    private String formattedMessage;          // After template is applied
}
