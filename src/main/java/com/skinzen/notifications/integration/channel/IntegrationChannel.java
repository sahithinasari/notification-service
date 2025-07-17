package com.skinzen.notifications.integration.channel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationChannel {
    @Bean
    public PublishSubscribeChannel combinedInputChannel() {
        return new PublishSubscribeChannel();
    }
    @Bean
    public MessageChannel notificationProcessingChannel() {
        return MessageChannels.direct().getObject();
    }


    // 🔹 Channels for different notification types
    @Bean
    public MessageChannel orderPlacedChannel() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public MessageChannel orderShippedChannel() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public MessageChannel defaultNotificationChannel() {
        return MessageChannels.direct().getObject();
    }
}
