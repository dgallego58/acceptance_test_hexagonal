package co.com.bancolombia.rabbitmq.properties;

import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class RabbitMQProperties {

    private String host;
    private int port;
    private String user;
    private String password;
    private Map<QueueNames, String> queues = new EnumMap<>(QueueNames.class);
    private Map<ExchangeNames, String> exchanges = new EnumMap<>(ExchangeNames.class);
    private Map<RoutingKeys, String> routingKeys = new EnumMap<>(RoutingKeys.class);

}
