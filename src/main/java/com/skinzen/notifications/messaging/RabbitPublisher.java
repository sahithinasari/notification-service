package com.skinzen.notifications.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.api.dto.NotificationRequest;
import com.skinzen.notifications.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.skinzen.notifications.constants.RabbitMQConstants.EVENTS_EXCHANGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publish(NotificationRequest request) {
        for (NotificationType channel : request.getChannels()) {
            try {
                String json = objectMapper.writeValueAsString(request);

            String routingKey =
                    "notification." +
                            request.getEventType().name().toLowerCase() +
                            "." + channel.name().toLowerCase();
            // event_type is added in routing for separation of concerns for each message
                // instead of having to inspect payload for event type
            log.info("Sending the notification");
            rabbitTemplate.convertAndSend(
                    EVENTS_EXCHANGE,
                    routingKey,
                    json
            );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
