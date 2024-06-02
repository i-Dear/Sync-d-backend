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
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3UploaderAdaptor implements S3Port {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Optional<String> uploadMultipartFileToS3(MultipartFile multipartFile) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
            return Optional.of(amazonS3.getUrl(bucket, newFilename).toString());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public Optional<Boolean> deleteFileFromS3(String filename){
        try {
            amazonS3.deleteObject(bucket, filename);
            return Optional.of(true); // 파일 삭제 성공
        } catch (Exception e) {
            return Optional.of(false); // 파일 삭제 실패
        }
    }
}
