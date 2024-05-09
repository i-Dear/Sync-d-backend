package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface UpdateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateProjectResponseDto updateProject(String userId,  String projectId,
                                           String projectName,
                                           String description,
                                           String image );

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            @NotBlank(message = ValidationMessages.PROJECT_NAME_NOT_BLANK)
            String projectName,
            @NotBlank(message = ValidationMessages.DESCRIPTION_NOT_BLANK)
            String description,
            String image

    ) {
    }

    record UpdateProjectResponseDto(
            String projectId
    ) {
    }

}
