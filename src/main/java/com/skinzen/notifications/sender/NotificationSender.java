package com.skinzen.notifications.sender;

import com.skinzen.notifications.dto.Notification;
import org.springframework.stereotype.Component;

public interface NotificationSender {
    void send(Notification notification);
}

