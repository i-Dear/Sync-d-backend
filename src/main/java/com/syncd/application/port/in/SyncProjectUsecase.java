package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface SyncProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    SyncProjectResponseDto syncProject(String userId, String projectId, int projectStage);
    // ======================================
    // DTO
    // ======================================

    record SyncProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            @NotNull(message = ValidationMessages.PROJECT_PROGRESS_NOT_NULL)
            Integer projectStage
    ){}

    record SyncProjectResponseDto(
            String projectId
    ){}
}
