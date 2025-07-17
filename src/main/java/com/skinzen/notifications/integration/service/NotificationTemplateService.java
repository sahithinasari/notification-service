package com.skinzen.notifications.integration.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationTemplateService {

    /**
     * Returns a template string for the given message type.
     * You can integrate with a DB or template file later.
     */
    public String getTemplateFor(String messageType) {
        // Simple switch-case or map for demo; can later use Thymeleaf templates
        System.out.println("Messagetype "+messageType);
        return switch (messageType.toUpperCase()) {
            case "WELCOME" -> "Hello [[name]], welcome to Skinzen!";
            case "PASSWORD_RESET" -> "Hi [[name]], use code [[otp]] to reset your password.";
            default -> "Hello [[name]], this is a default notification.";
        };
    }
}
