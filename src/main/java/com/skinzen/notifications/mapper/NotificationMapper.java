package com.skinzen.notifications.mapper;

import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.dto.NotificationRequestDto;
import com.skinzen.notifications.enums.NotificationType;
import com.skinzen.notifications.integration.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationMapper {

    @Autowired
    private NotificationTemplateService templateService;
   // @Autowired private UserService userService;

    public Notification mapToNotification(NotificationRequestDto dto) {
        log.info("Dto: {}", dto);
        Notification notification = new Notification();
        notification.setType(NotificationType.valueOf(dto.getMessageType()));
        notification.setData(dto.getData());
        log.info("Notification type: {}", notification.getType());
        // Get template based on message type (hardcoded or from DB/config)
        notification.setTemplate(templateService.getTemplateFor(dto.getMessageType()));
        System.out.println("Notification template: " + notification.getTemplate());
        // Fetch email from user ID (later)
        notification.setRecipient(dto.getData().get("email").toString()); // For now: use dto.getData().get("email")

        return notification;
    }
}
