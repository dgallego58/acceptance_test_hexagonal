package co.com.bancolombia.batch.aws;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;

public class S3Service {


    private final S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void putInS3(String s3OjReference, InputStream inputStream) {
        var objectRequest = PutObjectRequest.builder().bucket("myBucket").key(s3OjReference)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromInputStream(inputStream, -1));
    }
}
