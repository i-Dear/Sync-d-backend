package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface GetAllUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetAllUserResponseDto getAllUser(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId
    );


    // ======================================
    // DTO
    // ======================================

    record GetAllUserResponseDto(
            List<UserEntity> userEntities
    ) {}
}
