package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.rabbitmq.properties.ExchangeNames;
import co.com.bancolombia.rabbitmq.properties.QueueNames;
import co.com.bancolombia.rabbitmq.properties.RabbitMQProperties;
import co.com.bancolombia.rabbitmq.properties.RoutingKeys;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesAndTopicExchangeConfig {


    @Bean
    public TopicExchange appExchange(RabbitMQProperties rabbitMQProperties) {
        return new TopicExchange(rabbitMQProperties.getExchanges().get(ExchangeNames.WORKER_EXC));
    }

    @Bean
    public Queue appQueueGeneric(RabbitMQProperties rabbitMQProperties) {
        return new Queue(rabbitMQProperties.getQueues().get(QueueNames.WORKER_1));
    }

    @Bean
    public Binding appBindingGeneric(RabbitMQProperties rabbitMQProperties,
                                     Queue appQueueGeneric,
                                     TopicExchange appExchange) {
        return BindingBuilder.bind(appQueueGeneric)
                .to(appExchange)
                .with(rabbitMQProperties.getRoutingKeys().get(RoutingKeys.WORKER_ROUTE));
    }

}
