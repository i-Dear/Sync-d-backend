package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.enums.UserAccountStatus;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpdateUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    UpdateUserResponseDto updateUser(
            String adminId,
            String userId, String email,
            String name,
            String status,
            MultipartFile profileImg,
            String projectIdsJson
    );

    record UpdateUserRequestDto(
            String userId,
            String email,
            String name,
            String status,
            MultipartFile profileImg,
            String projectIdsJson
    ) {}


    record UpdateUserResponseDto(
            UserEntity userEntity
    ) {}
}
