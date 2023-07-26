package co.com.bancolombia.sftp.uploader;

import co.com.bancolombia.sftp.config.SftpConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class SftpService {

    private final SftpConfiguration.TransferGateway transferGateway;

    public SftpService(SftpConfiguration.TransferGateway transferGateway) {
        this.transferGateway = transferGateway;
    }

    //@Scheduled(fixedDelay = 5_000L)
    public void uploadFile() {
        File file;
        try {
            file = File.createTempFile("_acceptance_test_demo", ".csv");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        file.deleteOnExit();
        log.info("Loading file with {}", file.getName());
        transferGateway.sendToSftp(file);
    }


}
