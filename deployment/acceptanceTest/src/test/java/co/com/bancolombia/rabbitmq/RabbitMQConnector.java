package co.com.bancolombia.rabbitmq;

import co.com.bancolombia.aws.CredentialManager;
import co.com.bancolombia.aws.RabbitMQProperties;
import com.intuit.karate.Logger;
import com.intuit.karate.core.ScenarioEngine;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;


public class RabbitMQConnector {

    private static final Logger log = ScenarioEngine.get().logger;

    private final ConnectionFactory connectionFactory;
    private final AtomicReference<String> actualMessage;
    private final Channel channelListener;

    public RabbitMQConnector(String environment, String queue) throws IOException, TimeoutException {
        this.connectionFactory = new ConnectionFactory();
        Predicate<String> isNull = Objects::isNull;
        var env = isNull.or("local"::equals).test(environment);
        log.info("Running in local mode? {}, actual env is {}", env, environment);

        RabbitMQProperties rabbitMQProperties = env ?
                CredentialManager.of().localRabbit() : CredentialManager.of().rabbitMQProperties();
        connectionFactory.setHost(rabbitMQProperties.getHost());
        connectionFactory.setPort(rabbitMQProperties.getPort());
        connectionFactory.setUsername(rabbitMQProperties.getUser());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        actualMessage = new AtomicReference<>();
        log.info("Listening to queue [{}]", queue);

        //el canal debe estar abierto durante toda la instancia del test,
        // ya que la cola terminará encolada con un publisher
        this.channelListener = connectionFactory.newConnection().createChannel();
        listenTo(queue);
    }


    public void publish(String queue, String message) {
        log.info("Publishing message {} to queue {}", message, queue);
        try (var connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            log.info("Declaring queue");

            channel.queueDeclare(queue, false, false, false, null);
            log.info("Declaring exchange");
            channel.exchangeDeclare("worker.exc.1", BuiltinExchangeType.TOPIC, false);
            log.info("Binding Exchange to queue");

            channel.queueBind(queue, "worker.exc.1", "worker.routing.1");
            channel.basicPublish("worker.exc.1", "worker.routing.1", null, message.getBytes(StandardCharsets.UTF_8));
            log.debug("Message put {}", message);
        } catch (IOException | TimeoutException e) {
            log.error("fallido: ", e);
            throw new RuntimeException(e);
        }
    }


    public void listenTo(String queue) {
        try {
            log.debug("Checking messages");
            channelListener.queueDeclare(queue, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                var msg = new String(message.getBody(), StandardCharsets.UTF_8);
                actualMessage.set(msg);
                log.info("Value in Actual Message '{}'", actualMessage.get());
            };
            var tag = channelListener.basicConsume(queue, true, deliverCallback, consumerTag -> {
            });
            log.info("Tag is {}", tag);
        } catch (IOException e) {
            log.error("fallido en listening", e);
            throw new RuntimeException(e);
        }
    }

    public String msg() {
        return actualMessage.get();
    }
}
