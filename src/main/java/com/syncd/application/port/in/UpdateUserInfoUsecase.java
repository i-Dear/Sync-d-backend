package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpdateUserInfoUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateUserInfoResponseDto updateUserInfo(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId,
            String name,
            MultipartFile img
    );

    // ======================================
    // DTO
    // ======================================
    record UpdateUserInfoRequestDto(
            String name,
            MultipartFile img
    ) {
    }

    record UpdateUserInfoResponseDto(
            String userId,
            String name,
            String img
    ) {
    }


}