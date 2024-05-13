package com.syncd.adapter.out.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.syncd.application.port.out.s3.S3Port;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class S3UploaderAdaptor implements S3Port {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadMultipartFileToS3(MultipartFile multipartFile, String Name, String projectName) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = generateHash(Name, projectName) + fileExtension; // Combine user ID and timestamp

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, newFilename).toString();
    }

    private String generateHash(String Name, String projectName) {
        try {
            String combined = Name + ":" + projectName;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));

            String hash = Base64.getEncoder().encodeToString(hashBytes);

            return hash;
        } catch (NoSuchAlgorithmException e) {
            // Handle the case where SHA-256 is not available
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
