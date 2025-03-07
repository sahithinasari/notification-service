package com.redshift.notifications.integration.channel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;

@Configuration
public class IntegrationChannel {
    @Bean
    public PublishSubscribeChannel combinedInputChannel() {
        return new PublishSubscribeChannel();
    }
}
