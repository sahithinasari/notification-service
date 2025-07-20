package com.skinzen.notifications.sender;

import com.skinzen.notifications.dto.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailSender implements NotificationSender {
    @Autowired
    private final JavaMailSender mailSender;
    public void send(Notification notification) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(notification.getRecipient());
            helper.setSubject(notification.getType().name());
            helper.setText(notification.getFormattedMessage(), true); // true enables HTML content

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("Sending email to: {}", notification.getRecipient());
        mailSender.send(mimeMessage);
    }
}