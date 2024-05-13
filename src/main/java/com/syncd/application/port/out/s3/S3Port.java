package com.syncd.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface S3Port {

    public Optional<String> uploadMultipartFileToS3(MultipartFile multipartFile, String name, String id);
    public Optional<Boolean> deleteFileFromS3(String filename);
}
