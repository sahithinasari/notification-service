package com.redshift.notifications.controller;

import com.redshift.notifications.model.Notification;
import com.redshift.notifications.service.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationProducer notificationProducer;

    @PostMapping("/send")
    public String sendNotification(@RequestBody Notification notification) {
        notificationProducer.sendNotification(notification);
        return "Notification sent!";
    }
}
