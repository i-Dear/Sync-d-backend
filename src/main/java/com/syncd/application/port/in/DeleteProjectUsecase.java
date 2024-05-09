package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface DeleteProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    DeleteProjectResponseDto deleteProject(String userId, String projectId);

    // ======================================
    // DTO
    // ======================================
    record DeleteProjectResponseDto(

            String projectId
    ){

    }
     record DeleteProjectRequestDto (
             @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
             String projectId
    ) {
    }
}



