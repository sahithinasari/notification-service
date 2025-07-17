package com.skinzen.notifications.mapper;

import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.dto.NotificationRequestDto;
import com.skinzen.notifications.enums.NotificationType;
import com.skinzen.notifications.exceptions.ApiError;
import com.skinzen.notifications.exceptions.notification.InvalidNotificationTypeException;
import com.skinzen.notifications.exceptions.notification.NotificationExceptionHandler;
import com.skinzen.notifications.integration.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class NotificationMapper {

    private static final String FALLBACK_EMAIL = "priyamvadhanasari@gmail.com";
    private final NotificationTemplateService templateService;

    public NotificationMapper(NotificationTemplateService templateService) {
        this.templateService = templateService;
    }

    public Notification mapToNotification(NotificationRequestDto dto) {
        try {
            validateRequest(dto);

            NotificationType type = resolveNotificationType(dto.getMessageType());
            String template = templateService.getTemplateFor(dto.getMessageType());
            String recipient = extractRecipient(dto.getData());

            Notification notification = new Notification();
            notification.setType(type);
            notification.setTemplate(template);
            notification.setData(dto.getData());
            notification.setRecipient(recipient);

            return notification;
        } catch (Exception ex) {
            ApiError error = NotificationExceptionHandler.handle(ex, "NotificationMapper.mapToNotification");
            log.error("Mapping failed: {}", error);
            throw ex; // Rethrow if you want to fail fast; or return null/optional based on strategy
        }
    }

    private void validateRequest(NotificationRequestDto dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }

        if (dto.getMessageType() == null || dto.getMessageType().isBlank()) {
            throw new IllegalArgumentException("messageType is required");
        }
    }

    private NotificationType resolveNotificationType(String messageType) {
        try {
            return NotificationType.valueOf(messageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidNotificationTypeException("Unsupported message type: " + messageType);
        }
    }

    private String extractRecipient(Map<String, Object> data) {
        if (data != null && data.containsKey("email")) {
            return data.get("email").toString();
        }
        return FALLBACK_EMAIL;
    }
}
