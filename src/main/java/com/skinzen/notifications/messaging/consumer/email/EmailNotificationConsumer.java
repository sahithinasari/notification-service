package com.skinzen.notifications.messaging.consumer.email;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.api.dto.NotificationRequest;
import com.skinzen.notifications.exceptions.PermanentException;
import com.skinzen.notifications.exceptions.TransientException;
import com.skinzen.notifications.service.email.EmailService;
import com.skinzen.notifications.service.idempotency.IdempotencyService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final IdempotencyService idempotencyService;

    public EmailNotificationConsumer(
            EmailService emailService,
            ObjectMapper objectMapper,
            @Qualifier("redisIdempotencyService") IdempotencyService idempotencyService) {

        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.idempotencyService = idempotencyService;
    }

    @RabbitListener(queues = "email.queue")
    @Retryable(
            retryFor = TransientException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void consume(String message) {

        NotificationRequest request;
        try {
            request = objectMapper.readValue(message, NotificationRequest.class);
        } catch (JsonProcessingException e) {
            // permanent → DLQ immediately
            throw new PermanentException("Invalid JSON");
        }

        String key = "notification:" +
                request.getNotificationId() + ":EMAIL";

        if (idempotencyService.isDuplicate(key)) {
            log.warn("🔁 Duplicate EMAIL ignored for notificationId={}",
                    request.getNotificationId());
            return; // safe ACK
        }

        if (!isValidEmail(request.getRecipient())) {
            // permanent → DLQ
            throw new PermanentException(
                    "Invalid email: " + request.getRecipient()
            );
        }

        try {
            emailService.sendEmail(request);
            log.info("✅ Email sent successfully to {}", request.getRecipient());
        } catch (MessagingException e) {
            // transient → retry, then DLQ
            throw new TransientException("SMTP failure", e);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null &&
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
