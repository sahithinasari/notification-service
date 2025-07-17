package com.skinzen.notifications.integration.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.dto.NotificationRequestDto;
import com.skinzen.notifications.integration.sender.NotificationSender;
import com.skinzen.notifications.integration.service.NotificationFactory;
import com.skinzen.notifications.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;


@Configuration
public class NotificationIntegrationFlow {

    private static final Logger log = LoggerFactory.getLogger(NotificationIntegrationFlow.class);

  //  private final TemplateEngine templateEngine;
    private final NotificationFactory notificationFactory;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    public NotificationIntegrationFlow(NotificationFactory notificationFactory,
                                       NotificationMapper notificationMapper,
                                       ObjectMapper objectMapper) {
       // this.templateEngine = templateEngine;
        this.notificationFactory = notificationFactory;
        this.notificationMapper = notificationMapper;
        this.objectMapper = objectMapper;
    }

    // STEP 2: Global Error Handling Flow
    @Bean
    public IntegrationFlow errorHandlingFlow() {
        return IntegrationFlow
                .from("errorChannel")
                .handle(message -> {
                    Throwable error = (Throwable) message.getPayload();
                    log.error("Global Integration error: {}", error.getMessage(), error);
                    // TODO: Optionally: Send to dead-letter queue, alert, audit, etc.
                })
                .get();
    }
    @Bean
    public IntegrationFlow notificationFlow() {
        return IntegrationFlow
                .from("combinedInputChannel")
                .transform(Message.class, message -> {
                    String json = (String) message.getPayload();
                    log.info("Received notification JSON: {}", json);
                    NotificationRequestDto dto = null;
                    try {
                        dto = objectMapper.readValue(json, NotificationRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return notificationMapper.mapToNotification(dto);
                })
                .transform(templateApplier(stringTemplateEngine()))
                .handle(notificationDispatcher())
                .get();
    }

    @Bean
    public GenericTransformer<Notification, Notification> templateApplier(SpringTemplateEngine stringTemplateEngine) {
        return notification -> {
            Context context = new Context();
            context.setVariables(notification.getData());

            String rendered = stringTemplateEngine.process(notification.getTemplate(), context);
            log.info("Rendered template: {}", rendered);

            notification.setFormattedMessage(rendered);
            return notification;
        };
    }



    @Bean
    public MessageHandler notificationDispatcher() {
        return message -> {
            Notification notification = (Notification) message.getPayload();
            log.info("Dispatching notification: {}", notification);
            NotificationSender sender = notificationFactory.resolveSender(notification);
            sender.send(notification);
        };
    }

    @Bean
    public MessageHandler logNotificationEvent() {
        return message -> {
            Notification notification = (Notification) message.getPayload();
//            NotificationEvent event = NotificationEvent.builder()
//                    .type(notification.getType().name())
//                    .recipient(notification.getRecipient())
//                    .timestamp(Instant.now())
//                    .message(notification.getFormattedMessage())
//                    .build();
            log.info("Notification sent: {}", notification);
        };
    }
    @Bean
    public SpringTemplateEngine stringTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();

        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("TEXT"); // or "TEXT"
        resolver.setCacheable(false);     // Since it's dynamic

        engine.setTemplateResolver(resolver);
        return engine;
    }

}
