package org.cmc.curtaincall.web.service.image.store;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.cmc.curtaincall.web.exception.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
@Primary
@Profile({"prod", "dev"})
public class AwsS3ImageStore implements ImageStore {

    private final String bucketName;

    private final S3Client s3Client;

    private final S3Template s3Template;

    public AwsS3ImageStore(
            @Value("${app.s3.bucket-name}") String bucketName, S3Client s3Client, S3Template s3Template) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
        this.s3Template = s3Template;
    }

    public String store(Resource image, String storedName) {
        try (InputStream input = image.getInputStream()) {
            String filename = Optional.ofNullable(image.getFilename())
                    .orElseThrow(() -> new InvalidImageException("filename=" + image.getFilename()));
            String contentType = Files.probeContentType(Path.of(filename));
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(contentType)
                    .build();
            S3Resource resource = s3Template.upload(bucketName, storedName, input, metadata);
            return resource.getURL().toString();
        } catch (IOException e) {
            throw new InvalidImageException(e);
        }
    }

}
