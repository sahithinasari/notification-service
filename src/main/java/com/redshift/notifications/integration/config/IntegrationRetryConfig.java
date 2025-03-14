package com.redshift.notifications.integration.config;

import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.mail.MailException;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class IntegrationRetryConfig {

    @Bean
    public RequestHandlerRetryAdvice retryAdvice() {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
        advice.setRetryTemplate(retryTemplate());
        return advice;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .retryOn(MessagingException.class) // Specify the exception to retry on
                .maxAttempts(3) // Retry up to 3 times
                .fixedBackoff(2000) // Wait 2 seconds before retrying
                .build();
    }

}
