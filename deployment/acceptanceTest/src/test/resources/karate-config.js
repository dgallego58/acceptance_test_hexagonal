function fn() {
    let env = karate.env;

    const rabbitMq = Java.type('co.com.bancolombia.rabbitmq.RabbitMQConnector');
    const config = {
        rmq: new rabbitMq(env, 'worker.queue.1')
    };
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);
    return config;
}
