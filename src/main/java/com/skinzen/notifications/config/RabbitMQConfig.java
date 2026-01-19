package com.skinzen.notifications.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.skinzen.notifications.constants.RabbitMQConstants.*;

@Configuration
public class RabbitMQConfig {


    // =========================================================
    // Exchanges
    // =========================================================

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLX, true, false);
    }

    // =========================================================
    // EMAIL QUEUES
    // =========================================================

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "email.retry")
                .build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(notificationExchange())
                .with("notification.*.email");
    }

    @Bean
    public Queue emailRetryQueue() {
        return QueueBuilder.durable(EMAIL_RETRY_QUEUE)
                .withArgument("x-message-ttl", 10000)
                .withArgument("x-dead-letter-exchange", EVENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notification.retry.email")
                .build();
    }

    @Bean
    public Binding emailRetryBinding() {
        return BindingBuilder.bind(emailRetryQueue())
                .to(retryExchange())
                .with("email.retry");
    }

    @Bean
    public Queue emailDLQ() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    @Bean
    public Binding emailDLQBinding() {
        return BindingBuilder.bind(emailDLQ())
                .to(dlqExchange())
                .with("email.dlq");
    }

    // =========================================================
    // SMS QUEUES
    // =========================================================

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE)
                .withArgument("x-dead-letter-exchange", RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "sms.retry")
                .build();
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue())
                .to(notificationExchange())
                .with("notification.*.sms");
    }

    @Bean
    public Queue smsRetryQueue() {
        return QueueBuilder.durable(SMS_RETRY_QUEUE)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", EVENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notification.retry.sms")
                .build();
    }

    @Bean
    public Binding smsRetryBinding() {
        return BindingBuilder.bind(smsRetryQueue())
                .to(retryExchange())
                .with("sms.retry");
    }

    @Bean
    public Queue smsDLQ() {
        return QueueBuilder.durable(SMS_DLQ).build();
    }

    @Bean
    public Binding smsDLQBinding() {
        return BindingBuilder.bind(smsDLQ())
                .to(dlqExchange())
                .with("sms.dlq");
    }
}
