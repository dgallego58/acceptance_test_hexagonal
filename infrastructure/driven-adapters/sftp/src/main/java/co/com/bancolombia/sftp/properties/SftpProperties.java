package co.com.bancolombia.sftp.properties;

import lombok.Data;

@Data
public class SftpProperties {

    private String host;
    private int port;
    private String user;
    private String privateKey;
    private String baseDirectory;

}
