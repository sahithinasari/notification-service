package com.redshift.notifications.connections;

import com.redshift.notifications.util.RabbitMQProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    public RabbitMQConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(rabbitMQProperties.getHost(), rabbitMQProperties.getPort());
        factory.setUsername(rabbitMQProperties.getUsername());
        factory.setPassword(rabbitMQProperties.getPassword());
        return factory;
    }

    @Bean
    public Queue queue() {
        return new Queue(rabbitMQProperties.getQueue(), true);
    }


    //    @Bean
//    public DirectExchange exchange() {
//        // Declare an exchange
//        return new DirectExchange(rabbitMQProperties.getExchange());
//    }
//    @Bean
//    public Binding binding(Queue queue, DirectExchange exchange) {
//        // Bind the queue to the exchange with a routing key
//        return BindingBuilder.bind(queue).to(exchange).with(rabbitMQProperties.getRoutingKey());
//    }
}
