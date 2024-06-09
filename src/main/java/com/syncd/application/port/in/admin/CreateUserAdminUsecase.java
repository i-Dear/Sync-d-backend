package com.syncd.application.port.in.admin;

import com.syncd.enums.UserAccountStatus;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface CreateUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    CreateUserResponseDto addUser(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId,
            String email, String name, UserAccountStatus status, String profileImg, List<String> projectIds);

    // ======================================
    // DTO
    // ======================================
    record CreateUserRequestDto(
            String email,
            String name,
            UserAccountStatus status,
            String profileImg,
            List<String> projectIds
    ) {}


    record CreateUserResponseDto(
            String userId
    ) {}
}
