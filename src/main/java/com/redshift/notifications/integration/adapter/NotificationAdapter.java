package com.redshift.notifications.integration.adapter;

import com.redshift.notifications.model.Notification;
import com.redshift.notifications.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAdapter {

    private final EmailService emailService;
    private final RequestHandlerRetryAdvice retryAdvice;

    @ServiceActivator(inputChannel = "orderPlacedChannel", adviceChain = "retryAdvice")
    public void sendOrderPlacedNotification(Notification notification) throws MessagingException {
        sendEmail(notification, "Order Placed Notification");
    }

    @ServiceActivator(inputChannel = "orderShippedChannel", adviceChain = "retryAdvice")
    public void sendOrderShippedNotification(Notification notification) throws MessagingException {
        sendEmail(notification, "Order Shipped Notification");
    }

    @ServiceActivator(inputChannel = "defaultNotificationChannel", adviceChain = "retryAdvice")
    public void defaultOrderHandle(Notification notification) throws MessagingException {
        sendEmail(notification, "Order Processing Failed Notification");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    private void sendEmail(Notification notification, String subject) throws MessagingException {
        try {
            if (!isValidEmail(notification.getRecipient())) {
                log.warn("Invalid email format: {}. Skipping email.", notification.getRecipient());
                return;
            }
            log.info("Sending '{}' email to {}: {}", subject, notification.getRecipient(), notification.getMessage());
            emailService.sendEmail(notification.getRecipient(), subject, notification.getMessage());
            log.info("Email sent successfully!");
        } catch (MailAuthenticationException e) {
            log.error("SMTP authentication failed. Shutting down.");
            System.exit(1); // Stop app on authentication failure
        } catch (SendFailedException e) {
            log.error("Invalid email: {}. Skipping retries.", notification.getRecipient());
            return; // Prevents retry for invalid recipients
        } catch (MessagingException | MailException e) {
            log.error("Email send failed: {}", e.getMessage());
            throw e; // Allows retry for temporary failures
        }
    }
}
