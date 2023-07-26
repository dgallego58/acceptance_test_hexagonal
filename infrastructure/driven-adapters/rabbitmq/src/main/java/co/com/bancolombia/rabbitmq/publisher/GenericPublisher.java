package co.com.bancolombia.rabbitmq.publisher;

import co.com.bancolombia.rabbitmq.properties.ExchangeNames;
import co.com.bancolombia.rabbitmq.properties.RabbitMQProperties;
import co.com.bancolombia.rabbitmq.properties.RoutingKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class GenericPublisher {


    private final RabbitTemplate rabbitTemplate;
    private final Map<RoutingKeys, String> routingKeys;
    private final Map<ExchangeNames, String> exchangeNames;

    public GenericPublisher(RabbitTemplate rabbitTemplate, RabbitMQProperties rabbitMQProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.routingKeys = rabbitMQProperties.getRoutingKeys();
        this.exchangeNames = rabbitMQProperties.getExchanges();
    }

    public <T> void send(T message) {
        String exchange = exchangeNames.get(ExchangeNames.WORKER_EXC);
        String routingKey = routingKeys.get(RoutingKeys.WORKER_ROUTE);
        log.info("Sending message to exchange: {} with routingKey {}", exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
