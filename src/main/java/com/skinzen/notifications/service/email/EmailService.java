package com.skinzen.notifications.service.email;

import com.skinzen.notifications.api.dto.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    public void sendEmail(NotificationRequest request) throws MessagingException {

        String htmlBody = templateService.render(request);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getRecipient());
            helper.setSubject(subjectFor(request.getEventType().name()));
            helper.setText(htmlBody, true);

            mailSender.send(message);

    }

    private String subjectFor(String eventType) {
        return switch (eventType) {
            case "ORDER_PLACED" -> "Your order has been placed";
            case "ORDER_SHIPPED" -> "Your order has been shipped";
            default -> "Notification from Skinzen";
        };
    }
}
