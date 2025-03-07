package com.redshift.notifications.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ActiveMQProperties {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${jms.listener.retry.max-attempts}")
    private long maxAttempts;

    @Value("${jms.listener.retry.interval}")
    private long retryInterval;
}
