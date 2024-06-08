package com.syncd.application.port.in.admin;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface DeleteUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    DeleteUserResponseDto deleteUser(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId,
            String userId
    );

    // ======================================
    // DTO
    // ======================================
    record DeleteUserRequestDto(
            String userId
    ) {}


    record DeleteUserResponseDto(
            String userId
    ) {}
}
