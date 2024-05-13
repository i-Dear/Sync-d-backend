package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

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