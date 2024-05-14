package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface JoinProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    JoinProjectResponseDto joinProject(String userId, String projectId);

    // ======================================
    // DTO
    // ======================================
    record JoinProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId
    ) {
    }

    record JoinProjectResponseDto(
            String projectId
    ) {
    }

}