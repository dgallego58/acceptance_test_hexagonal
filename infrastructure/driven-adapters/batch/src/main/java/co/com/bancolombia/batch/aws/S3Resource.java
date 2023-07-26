package co.com.bancolombia.batch.aws;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.WritableResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class S3Resource extends AbstractResource implements WritableResource {

    private final ByteArrayOutputStream resource;

    public S3Resource() {
        this.resource = new ByteArrayOutputStream();
    }


    @Override
    public String getDescription() {
        return "The s3 resource implementation";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(resource.toByteArray());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return resource;
    }
}
