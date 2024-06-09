package com.syncd.application.port.in.admin;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface DeleteProjectAdminUsecase {
    // ======================================
    // METHOD
    // ======================================
    DeleteProjectAdminResponseDto deleteProject(
            String adminId,
            String projectId
    );

    // ======================================
    // DTO
    // ======================================
    record DeleteProjectAdminResponseDto(
            String projectId
    ){

    }
    record DeleteProjectAdminRequestDto (
            String projectId
    ) {
    }
}
