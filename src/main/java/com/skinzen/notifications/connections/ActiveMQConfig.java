package com.skinzen.notifications.connections;

import com.skinzen.notifications.util.ActiveMQProperties;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

    private final ActiveMQProperties activeMQProperties;

    public ActiveMQConfig(ActiveMQProperties activeMQProperties) {
        this.activeMQProperties = activeMQProperties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(activeMQProperties.getBrokerUrl());
        factory.setUserName(activeMQProperties.getUsername());
        factory.setPassword(activeMQProperties.getPassword());
        return factory;
    }
}
