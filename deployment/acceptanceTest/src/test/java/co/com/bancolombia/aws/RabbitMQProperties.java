package co.com.bancolombia.aws;

import lombok.Data;

@Data
public class RabbitMQProperties {
    private String host;
    private int port;
    private String user;
    private String password;

}
