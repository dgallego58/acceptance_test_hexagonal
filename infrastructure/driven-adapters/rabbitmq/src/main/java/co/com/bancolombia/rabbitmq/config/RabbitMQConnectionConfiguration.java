package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.rabbitmq.properties.RabbitMQProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConnectionConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory,
                                         Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        var template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter);
        return template;
    }

    @Bean
    public ConnectionFactoryCustomizer factoryCustomizer(RabbitMQProperties rabbitMQProperties) {
        return factory -> {
            factory.setHost(rabbitMQProperties.getHost());
            factory.setPort(rabbitMQProperties.getPort());
            factory.setUsername(rabbitMQProperties.getUser());
            factory.setPassword(rabbitMQProperties.getPassword());
        };
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    public RabbitMQProperties rabbitMQProperties() {
        return new RabbitMQProperties();
    }

}
