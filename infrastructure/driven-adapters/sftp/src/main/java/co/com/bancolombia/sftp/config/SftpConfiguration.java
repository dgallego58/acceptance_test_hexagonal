package co.com.bancolombia.sftp.config;

import co.com.bancolombia.sftp.properties.SftpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.dsl.Sftp;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class SftpConfiguration {


    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory(SftpProperties sftpProperties) {
        log.info(sftpProperties.toString());
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpProperties.getHost());
        factory.setPort(sftpProperties.getPort());
        factory.setUser(sftpProperties.getUser());
        factory.setAllowUnknownKeys(true);
        Resource resource = new DefaultResourceLoader().getResource(sftpProperties.getPrivateKey());
        try {
            log.info(resource.getContentAsString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UnableToLoadPrivateKeyException(e);
        }
        factory.setPrivateKey(resource);
        return new CachingSessionFactory<>(factory);
    }

    @Bean
    @ConfigurationProperties(prefix = "sftp")
    public SftpProperties sftpProperties() {
        return new SftpProperties();
    }

    @Bean
    public IntegrationFlow sftpOutBoundFlow(SessionFactory<SftpClient.DirEntry> sessionFactory, SftpProperties properties) {
        return IntegrationFlow.from("toSftpChannel")
                .handle(Sftp.outboundAdapter(sessionFactory, FileExistsMode.REPLACE)
                        .useTemporaryFileName(false)
                        .remoteDirectory(properties.getBaseDirectory())
                        .fileNameGenerator(message -> {
                            if (message.getPayload() instanceof File file) {
                                return file.getName();
                            }
                            throw new IllegalArgumentException("File expected in message payload");
                        }))
                .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public MessageHandler handler(SessionFactory<SftpClient.DirEntry> sessionFactory, SftpProperties properties) {
        SftpMessageHandler handler = new SftpMessageHandler(sessionFactory);
        handler.setRemoteDirectoryExpression(new LiteralExpression(properties.getBaseDirectory()));
        handler.setLoggingEnabled(true);
        handler.setFileNameGenerator(message -> {
            if (message.getPayload() instanceof File file) {
                return file.getName();
            }
            throw new IllegalArgumentException("File expected in message payload");
        });

        return handler;
    }

    @MessagingGateway
    public interface TransferGateway {
        @Gateway(requestChannel = "toSftpChannel")
        void sendToSftp(File file);
    }

    public static class UnableToLoadPrivateKeyException extends RuntimeException {
        public UnableToLoadPrivateKeyException(Throwable cause) {
            super(cause);
        }
    }

}
