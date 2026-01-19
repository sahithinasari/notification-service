package com.skinzen.notifications.constants;

public class RabbitMQConstants {
    // ---------------------------
    // Exchange Names
    // ---------------------------
    public static final String EVENTS_EXCHANGE = "notification.events.exchange";
    public static final String RETRY_EXCHANGE = "notification.retry.exchange";
    public static final String DLX = "notification.dlx";

    // ---------------------------
    // Queue Names
    // ---------------------------
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_RETRY_QUEUE = "email.retry.queue";
    public static final String EMAIL_DLQ = "email.dlq.queue";

    public static final String SMS_QUEUE = "sms.queue";
    public static final String SMS_RETRY_QUEUE = "sms.retry.queue";
    public static final String SMS_DLQ = "sms.dlq.queue";

}
