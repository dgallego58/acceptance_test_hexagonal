package co.com.bancolombia.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.karate.Logger;
import com.intuit.karate.core.ScenarioEngine;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.util.function.Supplier;

public class CredentialManager {

    public static CredentialManager of(){
        return new CredentialManager();
    }


    public Supplier<SecretsManagerClient> secrets() {
        return () -> SecretsManagerClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public RabbitMQProperties rabbitMQProperties() {
        try (SecretsManagerClient secrets = secrets().get()) {
            var secretsAsJson = secrets.getSecretValue(builder -> builder.secretId("myId"))
                    .secretString();
            return new ObjectMapper().readValue(secretsAsJson, RabbitMQProperties.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger logger() {
        ScenarioEngine scenarioEngine = ScenarioEngine.get();
        return scenarioEngine.logger;
    }

    public RabbitMQProperties localRabbit() {
        logger().debug("Local Rabbit");
        var props = new RabbitMQProperties();
        props.setHost("localhost");
        props.setPort(5672);
        props.setUser("guest");
        props.setPassword("guest");
        return props;
    }

}
