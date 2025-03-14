package com.redshift.notifications.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.notifications.model.Notification;
import com.redshift.notifications.service.NotificationProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/activeMQ/send")
    public String sendNotificationViaActiveMQ(@RequestBody Notification notification) {
        notificationProducer.sendNotification(notification);
        return "Notification sent!";
    }
    @PostMapping("/rabbitMQ/send")
    public String sendNotificationViaRabbitMQ(@RequestBody Notification notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            rabbitTemplate.convertAndSend("notification.queue", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Notification sent via RabbitMQ!";
    }
}
