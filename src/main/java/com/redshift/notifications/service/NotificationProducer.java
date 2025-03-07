package com.redshift.notifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.notifications.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {
    private static final String QUEUE_NAME = "notificationQueue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendNotification(Notification notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            jmsTemplate.convertAndSend(QUEUE_NAME, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
