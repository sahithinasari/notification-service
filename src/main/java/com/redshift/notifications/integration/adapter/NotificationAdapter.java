package com.redshift.notifications.integration.adapter;

import com.redshift.notifications.model.Notification;
import com.redshift.notifications.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAdapter {

    private final EmailService emailService;

    private static final int MAX_ATTEMPTS = 3; // Max retries before stopping
    private static final int DELAY_BETWEEN_RETRIES_MS = 3000; // 3 seconds delay

    /**
     * Sends an email with retry logic for recoverable errors.
     */
    private void sendEmailWithRetry(Notification notification, String subject) {
        int attempt = 0;

        while (attempt < MAX_ATTEMPTS) {
            try {
                log.info("Attempt {}/{}: Sending '{}' email to {}: {}", attempt + 1, MAX_ATTEMPTS, subject, notification.getRecipient(), notification.getMessage());
                emailService.sendEmail(notification.getRecipient(), subject, notification.getMessage());
                log.info("Email sent successfully!");
                return; // Exit if successful
            } catch (MailAuthenticationException e) {
                handleAuthenticationFailure();
                throw e; // Stop retrying if authentication fails
            } catch (SendFailedException e) {
                log.error("Invalid email address: {}. Not retrying further.", notification.getRecipient());
                return; // Stop retrying if recipient is invalid
            } catch (MessagingException | MailException e) {
                log.error("Error sending email (attempt {}/{}): {}", attempt + 1, MAX_ATTEMPTS, e.getMessage());
                attempt++;

                if (attempt < MAX_ATTEMPTS) {
                    try {
                        Thread.sleep(DELAY_BETWEEN_RETRIES_MS); // Wait before retrying
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    log.error("Max retries reached. Email to {} failed permanently.", notification.getRecipient());
                }
            }
        }
    }

    /**
     * Handles SMTP authentication failures by shutting down after max retries.
     */
    private void handleAuthenticationFailure() {
        log.error("SMTP authentication failed. Shutting down the application.");
        System.exit(1);
    }

    @ServiceActivator(inputChannel = "orderPlacedChannel")
    public void sendOrderPlacedNotification(Notification notification) {
        sendEmailWithRetry(notification, "Order Placed Notification");
    }

    @ServiceActivator(inputChannel = "orderShippedChannel")
    public void sendOrderShippedNotification(Notification notification) {
        sendEmailWithRetry(notification, "Order Shipped Notification");
    }

    @ServiceActivator(inputChannel = "defaultNotificationChannel")
    public void defaultOrderHandle(Notification notification) {
        sendEmailWithRetry(notification, "Order Processing Failed Notification");
    }
}
