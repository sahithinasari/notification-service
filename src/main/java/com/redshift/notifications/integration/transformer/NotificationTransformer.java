package com.redshift.notifications.integration.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.notifications.model.Notification;
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

    @Transformer(inputChannel = "combinedInputChannel", outputChannel = "notificationProcessingChannel")
    public Message<Notification> transform(Message<String> message) {
        try {
            log.info("Transforming message: {}", message.getPayload());
            // we can transform teh message according to our requirement
            //we can also build a message payload instead of sending notification object via
            //channels
            Notification notification = objectMapper.readValue(message.getPayload(), Notification.class);

            // Set a custom header (for example, based on event type)
            String eventType = notification.getMessage(); // Assuming message contains the event type

            // Build a new message with the transformed payload and additional headers
            return MessageBuilder.withPayload(notification)
                    .setHeader("eventType", eventType)  // Custom header for routing
                    .setHeader(MessageHeaders.CONTENT_TYPE,"application/json")
                    .build();
            //return objectMapper.readValue(message.getPayload(), Notification.class);
        } catch (Exception e) {
            log.error("Error transforming message: {}", e.getMessage(), e);
            throw new RuntimeException("Message transformation failed", e);
        }
    }
}
