package com.skinzen.notifications.service.email;

import com.skinzen.notifications.api.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String render(NotificationRequest request) {

        Context context = new Context();
        context.setVariables(request.getParams());

        return templateEngine.process(
                "email/" + resolveTemplate(request.getEventType().name()),
                context
        );
    }

    private String resolveTemplate(String eventType) {
        return switch (eventType) {
            case "ORDER_PLACED" -> "order-placed";
            case "ORDER_SHIPPED" -> "order-shipped";
            case "OTP" -> "otp";
            default -> "default";
        };
    }
}

