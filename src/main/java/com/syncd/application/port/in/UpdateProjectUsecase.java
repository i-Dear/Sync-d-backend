package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface UpdateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateProjectResponseDto updateProject(String userId, String projectId,
                                           String projectName,
                                           String description,
                                           String image );

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            String projectName,
            String description,
            String image
    ) {
    }

    record UpdateProjectResponseDto(
            String projectId
    ) {
    }

}
