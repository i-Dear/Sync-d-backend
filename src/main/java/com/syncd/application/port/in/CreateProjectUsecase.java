package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface CreateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectResponseDto createProject(String userId, String name, String description, String img, List<String> userEmails);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectRequestDto(
            @NotBlank(message = ValidationMessages.NAME_NOT_BLANK)
            String name,
            @NotBlank(message = ValidationMessages.DESCRIPTION_NOT_BLANK)
            String description,
            String img,
            @NotNull(message = ValidationMessages.USERS_NOT_NULL)
            @Size(min = 1, message = ValidationMessages.USERS_SIZE)
            List<String> userEmails
    ) {
    }

    record CreateProjectResponseDto(
            String projectId
    ) {
    }

}