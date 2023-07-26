package co.com.bancolombia.rabbitmq.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenericSubscriber {

    /**
     * en la queue se usa una SpEL
     *
     * @param message
     */
    @RabbitListener(queues = {"#{@rabbitMQProperties.queues['WORKER_1']}"})
    public void receiveMessage(final Message message) {
        log.info("Received message from generic queue {}", message.toString());
    }

}
