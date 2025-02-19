package com.redshift.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.notifications.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "notificationInputChannel")
    public void receiveMessage(Message<String> message) {
        try {
            String payload = message.getPayload();
            log.info("Received Raw Message: {}", payload);

            if (payload.trim().startsWith("{")) {
                // Message is JSON, try to parse it
                Notification notification = objectMapper.readValue(payload, Notification.class);
                log.info("Parsed Notification: {} for {}", notification.getMessage(), notification.getRecipient());
            } else {
                // Plain text or unstructured data
                log.info("Received Text Message: {}", payload);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
