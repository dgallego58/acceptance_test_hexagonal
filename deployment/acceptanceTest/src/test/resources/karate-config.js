function fn() {
    let env = karate.env;


    const rabbitMq = Java.type('co.com.bancolombia.rabbitmq.RabbitMQConnector');

    let urlBase = '#{app-gateway-host}#'
    //let urlBase = '';
    if (env === 'local') {
        urlBase = 'http://localhost:8080'
    }
    if (env === 'dev') {
        urlBase = 'https://mp-vpn...'
    }

    const config = {
        rmq: new rabbitMq(env, 'worker.queue.1'),
        urlBase: urlBase
    };

    //acá va todo lo de replace tokens #{myvar}#
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);
    return config;
}
