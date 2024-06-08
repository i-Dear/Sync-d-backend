package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface GetAllProjectAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetAllProjectResponseDto getAllProject(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId
    );

    // ======================================
    // DTO
    // ======================================

    record GetAllProjectResponseDto(
            List<ProjectEntity> projectEntities
    ) {}
}
