package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GetOauthRedirectUrlUsecase {
    // ======================================
    // METHOD
    // ======================================
    String getOauthRedirectUrlUsecase(String referer);

}