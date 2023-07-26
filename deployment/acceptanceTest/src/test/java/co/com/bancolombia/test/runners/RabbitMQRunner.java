package co.com.bancolombia.test.runners;

import com.intuit.karate.junit5.Karate;

public class RabbitMQRunner {


    @Karate.Test
    Karate testConnectionPushAndConsume() {
        return Karate.run("rabbittest").relativeTo(getClass());
    }

}
