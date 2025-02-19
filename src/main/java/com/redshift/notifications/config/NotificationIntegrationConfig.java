package com.redshift.notifications.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsInboundGateway;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
@IntegrationComponentScan
public class NotificationIntegrationConfig {

    @Bean
    public DirectChannel notificationInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("notificationQueue");
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener() {
        ChannelPublishingJmsMessageListener listener = new ChannelPublishingJmsMessageListener();
        listener.setRequestChannel(notificationInputChannel());
        return listener;
    }

    @Bean
    public JmsInboundGateway inboundGateway(DefaultMessageListenerContainer container,
                                            ChannelPublishingJmsMessageListener listener) {
        return new JmsInboundGateway(container, listener);
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        return new MessagingTemplate(notificationInputChannel());
    }
}
