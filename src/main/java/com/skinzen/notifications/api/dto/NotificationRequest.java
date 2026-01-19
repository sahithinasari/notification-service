package com.skinzen.notifications.api.dto;

import com.skinzen.notifications.enums.EventType;
import com.skinzen.notifications.enums.NotificationType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotificationRequest {
    private String notificationId;
    private String userId;
    private EventType eventType;
    private List<NotificationType> channels;
    private String recipient;
    private Map<String, Object> params;
}


