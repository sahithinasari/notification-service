package com.skinzen.notifications.service;

import com.skinzen.notifications.api.dto.NotificationRequest;
import com.skinzen.notifications.messaging.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitPublisher rabbitPublisher;

    public void processNotification(NotificationRequest request) {
//        // 1. Validate template exists
//        templateService.validateTemplate(request.getTemplateId());
//
//        // 2. Check user preferences
//        preferenceService.filterDisabledChannels(request);
//
//        // 3. Store notification audit entry
//        NotificationEntity saved = repo.save(new NotificationEntity(request));
        request.setNotificationId(UUID.randomUUID().toString());
        rabbitPublisher.publish(request);
    }
}
