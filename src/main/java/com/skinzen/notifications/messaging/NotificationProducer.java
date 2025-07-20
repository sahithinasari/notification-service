package com.skinzen.notifications.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.dto.NotificationRequestDto;
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

    public void sendNotification(NotificationRequestDto notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            jmsTemplate.convertAndSend(QUEUE_NAME, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
