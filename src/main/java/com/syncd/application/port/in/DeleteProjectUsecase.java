package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

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



