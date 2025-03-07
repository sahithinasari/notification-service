package com.redshift.notifications.integration.inbound;

import com.redshift.notifications.util.ActiveMQProperties;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsInboundGateway;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class JmsInboundConfig {
    private ActiveMQProperties activeMQProperties;
    public JmsInboundConfig(ActiveMQProperties activeMQProperties){
        this.activeMQProperties=activeMQProperties;
    }

    @Bean
    public DirectChannel activeMqNotificationChannel() {
        return new DirectChannel();
    }

    @Bean
    public DefaultMessageListenerContainer activeMqListenerContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("notificationQueue");
        System.out.println("Interval"+activeMQProperties.getRetryInterval()+" "+activeMQProperties.getRetryInterval());
        FixedBackOff backOff = new FixedBackOff(activeMQProperties.getRetryInterval(),activeMQProperties.getMaxAttempts()); // Retry every 5s, max 3 attempts
        container.setBackOff(backOff);
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener activeMqMessageListener() {
        ChannelPublishingJmsMessageListener listener = new ChannelPublishingJmsMessageListener();
        listener.setRequestChannel(activeMqNotificationChannel());
        return listener;
    }

    @Bean
    public JmsInboundGateway activeMqInboundGateway(DefaultMessageListenerContainer container,
                                                    ChannelPublishingJmsMessageListener listener) {
        return new JmsInboundGateway(container, listener);
    }
}
