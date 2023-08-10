package org.cmc.curtaincall.web.service.image.store;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.cmc.curtaincall.web.exception.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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

    private final AmazonS3 s3Client;

    public AwsS3ImageStore(@Value("${cloud.aws.s3.bucket}") String bucketName, AmazonS3 s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    public String store(Resource image, String storedName) {
        try (InputStream input = image.getInputStream()) {
            String filename = Optional.ofNullable(image.getFilename())
                    .orElseThrow(() -> new InvalidImageException("filename=" + image.getFilename()));
            String contentType = Files.probeContentType(Path.of(filename));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.contentLength());
            metadata.setContentType(contentType);
            s3Client.putObject(new PutObjectRequest(bucketName, storedName, input, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(bucketName, storedName).toString();
        } catch (IOException e) {
            throw new InvalidImageException(e);
        }
    }

}
