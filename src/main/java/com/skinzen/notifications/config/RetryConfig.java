package com.skinzen.notifications.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
/*
* “Create AOP proxies for beans that use @Retryable.”
* Without this, @Retryable is just a normal annotation — it does nothing.
* ❌ Without @EnableRetry

Method is called once

Exception is thrown

No retry happens

Message is rejected immediately

Goes straight to DLQ*/
@Configuration
@EnableRetry
public class RetryConfig {
}
