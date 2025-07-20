package com.skinzen.notifications.service;

import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.sender.EmailSender;
import com.skinzen.notifications.sender.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationFactory {
    @Autowired
    private EmailSender emailSender;

    public NotificationSender resolveSender(Notification notification) {
        log.info("Resolving sender for notification type: {}", notification.getType());
        return switch (notification.getType().name()) {
            case "EMAIL" -> emailSender;
            // add more types like SMS, PUSH later
            default -> throw new IllegalArgumentException("Unsupported type");
        };
    }
}
