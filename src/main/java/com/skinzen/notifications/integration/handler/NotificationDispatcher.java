package com.skinzen.notifications.integration.handler;

import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.sender.NotificationSender;
import com.skinzen.notifications.service.NotificationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class NotificationDispatcher {

    private final NotificationFactory notificationFactory;
    private static final Logger log = LoggerFactory.getLogger(NotificationDispatcher.class);

    public NotificationDispatcher(NotificationFactory notificationFactory) {
        this.notificationFactory = notificationFactory;
    }

    public void dispatch(Message<Notification> message) {
        Notification notification = message.getPayload();
        log.info("Dispatching: {}", notification);
        NotificationSender sender = notificationFactory.resolveSender(notification);
        sender.send(notification);
    }
}
