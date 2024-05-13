package com.syncd.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Port {

    public String uploadMultipartFileToS3(MultipartFile multipartFile, String hostName, String ProjectName) throws IOException;
}
