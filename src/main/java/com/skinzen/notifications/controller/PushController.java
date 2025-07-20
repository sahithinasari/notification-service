package com.skinzen.notifications.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.dto.NotificationRequestDto;
import com.skinzen.notifications.messaging.NotificationProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class PushController {

    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/activeMQ/send")
    public String sendNotificationViaActiveMQ(@RequestBody NotificationRequestDto notification) {
        notificationProducer.sendNotification(notification);
        return "Notification sent!";
    }
    @PostMapping("/rabbitMQ/send")
    public String sendNotificationViaRabbitMQ(@RequestBody NotificationRequestDto notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            rabbitTemplate.convertAndSend("notification.queue", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Notification sent via RabbitMQ!";
    }
}
