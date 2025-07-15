package com.skinzen.notifications.integration.router;

import com.skinzen.notifications.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationRouter {

    @Router(inputChannel = "notificationProcessingChannel")
    public String routeNotification(Message<Notification> message) {
        String eventType = (String) message.getHeaders().get("eventType");

        log.info("Routing notification for event type: {}", eventType);

        return switch (eventType) {
            case "ORDER_PLACED" -> "orderPlacedChannel";
            case "ORDER_SHIPPED" -> "orderShippedChannel";
            default -> "defaultNotificationChannel";
        };
    }
}
