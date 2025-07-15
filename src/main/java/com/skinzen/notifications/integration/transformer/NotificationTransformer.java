package com.skinzen.notifications.integration.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.model.Notification;
import com.skinzen.notifications.service.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationTransformer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserServiceClient userClient;

    public NotificationTransformer(UserServiceClient userClient) {
        this.userClient = userClient;
    }

    @Transformer(inputChannel = "combinedInputChannel", outputChannel = "notificationProcessingChannel")
    public Message<Notification> transform(Message<String> message) {
        try {
            log.info("Transforming message: {}", message.getPayload());

            // Deserialize JSON message into Notification object
            Notification notification = objectMapper.readValue(message.getPayload(), Notification.class);

            // Fetch recipient email from User Service
            String recipientEmail = userClient.getUserEmail(notification.getRecipient());
            notification.setRecipient(recipientEmail);

            log.info("Recipient email resolved to: {}", recipientEmail);

            return MessageBuilder.withPayload(notification)
                    .setHeader("eventType", notification.getMessage()) // Set event type header
                    .setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
                    .build();
        } catch (Exception e) {
            log.error("Error transforming message: {}", e.getMessage(), e);
            throw new RuntimeException("Message transformation failed", e);
        }
    }
}
