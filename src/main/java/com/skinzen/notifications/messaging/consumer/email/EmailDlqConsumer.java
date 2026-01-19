package com.skinzen.notifications.messaging.consumer.email;

import com.skinzen.notifications.api.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailDlqConsumer {

    //private final NotificationStatusService statusService;

    @RabbitListener(queues = "email.dlq.queue")
    public void consumeDlq(NotificationRequest request) {

        log.error("❌ EMAIL moved to DLQ | notificationId={} | recipient={}",
                request.getNotificationId(),
                request.getRecipient()
        );

        // Persist FAILED status
//        statusService.markFailed(
//                request.getNotificationId(),
//                "EMAIL",
//                "Moved to DLQ after retries"
//        );

        // Simple alert (can later be Slack / PagerDuty)
        alert(request);
    }

    private void alert(NotificationRequest request) {
        log.error("🚨 ALERT: Email delivery failed permanently for user={} notificationId={}",
                request.getUserId(),
                request.getNotificationId()
        );
    }
}
