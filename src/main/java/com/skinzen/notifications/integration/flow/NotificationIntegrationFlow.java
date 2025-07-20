package com.skinzen.notifications.integration.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinzen.notifications.dto.Notification;
import com.skinzen.notifications.dto.NotificationRequestDto;
import com.skinzen.notifications.integration.handler.NotificationDispatcher;
import com.skinzen.notifications.mapper.NotificationMapper;
import com.skinzen.notifications.service.NotificationFactory;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.support.RetryTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Slf4j
@Configuration
public class NotificationIntegrationFlow {

    private final NotificationFactory notificationFactory;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    public NotificationIntegrationFlow(NotificationFactory notificationFactory,
                                       NotificationMapper notificationMapper,
                                       ObjectMapper objectMapper) {
        this.notificationFactory = notificationFactory;
        this.notificationMapper = notificationMapper;
        this.objectMapper = objectMapper;
    }

    @Bean
    public IntegrationFlow notificationFlow(NotificationDispatcher dispatcher,
                                            SpringTemplateEngine templateEngine) {
        return IntegrationFlow
                .from("combinedInputChannel")
                .transform(this::jsonToNotification)
                .transform(templateApplier(templateEngine))
                .handle(dispatcher, "dispatch", e -> e.advice(errorHandlingAdvice()))
                .get();
    }

    private Notification jsonToNotification(Message<?> message) {
        try {
            String json = (String) message.getPayload();
            return notificationMapper.mapToNotification(objectMapper.readValue(json, NotificationRequestDto.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON in Notification", e);
        }
    }

    @Bean
    public GenericTransformer<Notification, Notification> templateApplier(SpringTemplateEngine templateEngine) {
        return notification -> {
            Context context = new Context();
            context.setVariables(notification.getData());
            String rendered = templateEngine.process(notification.getTemplate(), context);
            notification.setFormattedMessage(rendered);
            return notification;
        };
    }

    @Bean
    public SpringTemplateEngine stringTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("TEXT");
        resolver.setCacheable(false);
        engine.setTemplateResolver(resolver);
        return engine;
    }

    // Handles all unhandled errors (globally)
    @Bean
    public IntegrationFlow errorFlow() {
        return IntegrationFlow.from("errorChannel")
                .handle(m -> {
                    Throwable t = (Throwable) m.getPayload();
                    log.error("Unhandled exception in flow: {}", t.getMessage(), t);
                }).get();
    }

    // Retry + Error Routing
    @Bean
    public Advice errorHandlingAdvice() {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
        ErrorMessageSendingRecoverer recoverer = new ErrorMessageSendingRecoverer(errorChannel());
        advice.setRecoveryCallback(recoverer);
        advice.setRetryTemplate(new RetryTemplate());
        return advice;
    }

    @Bean
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }
}

