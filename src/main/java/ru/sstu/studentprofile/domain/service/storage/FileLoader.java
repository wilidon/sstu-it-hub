package ru.sstu.studentprofile.domain.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.MultipartBodyBuilder;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public abstract class FileLoader implements Loader  {
    private final S3Client s3;
    private final String endpoint;
    private final String bucketName;

    public FileLoader(
            final String uploadEndpoint,
            final String endpoint,
            final String bucketName,
            final String accessKey,
            final String secretKey
    ) {
        final AwsSessionCredentials awsCredentials = AwsSessionCredentials.create(
                accessKey,
                secretKey,
                "");
        final Region region = Region.US_EAST_2;
        s3 = S3Client.builder().credentialsProvider(
                        StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(uploadEndpoint)).region(region).build();
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    String loadFile(String filePath, InputStream file, Map<String, String> metadata) {
        final MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);

        try {
            final PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .metadata(metadata)
                    .build();

            s3.putObject(putOb, RequestBody.fromInputStream(file,
                    file.available()));
        } catch (S3Exception | IOException e) {
            throw new RuntimeException(e);
        }
        return endpoint + filePath;
    }

    String loadFile(String filePath, InputStream file) {
        return this.loadFile(filePath, file, null);
    }
}
