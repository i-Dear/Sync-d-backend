package com.syncd.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface S3Port {

     Optional<String> uploadMultipartFileToS3(MultipartFile multipartFile);
     Optional<Boolean> deleteFileFromS3(String filename);
}
