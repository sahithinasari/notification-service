package com.skinzen.notifications.integration.inbound;

import com.skinzen.notifications.util.RabbitMQProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.PublishSubscribeChannel;

@Configuration
@ConditionalOnProperty(name = "integration.flow.source", havingValue = "rabbitmq")
public class RabbitMqInboundConfig {

    private final RabbitMQProperties rabbitMQProperties;
    private final PublishSubscribeChannel combinedInputChannel;

    public RabbitMqInboundConfig(RabbitMQProperties rabbitMQProperties, PublishSubscribeChannel combinedInputChannel) {
        this.rabbitMQProperties = rabbitMQProperties;
        this.combinedInputChannel=combinedInputChannel;
    }

//    @Bean
//    public MessageChannel rabbitMqNotificationChannel() {
//        return new DirectChannel();
//    }

    @Bean
    public Queue rabbitQueue() {
        return new Queue(rabbitMQProperties.getQueue(), true);
    }

    @Bean
    public SimpleMessageListenerContainer rabbitListenerContainer(ConnectionFactory rabbitConnectionFactory, Queue rabbitQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setQueues(rabbitQueue);
        return container;
    }

    @Bean
    public AmqpInboundChannelAdapter inboundRabbitMqListener(SimpleMessageListenerContainer container) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(container);
        adapter.setOutputChannel(combinedInputChannel);
        return adapter;
    }
}
