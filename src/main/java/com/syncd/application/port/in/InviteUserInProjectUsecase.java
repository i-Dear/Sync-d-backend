package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface InviteUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    InviteUserInProjectResponseDto inviteUserInProject(String userId, String projectId, List<String> users);
    // ======================================
    // DTO
    // ======================================
    record InviteUserInProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            @NotNull(message = ValidationMessages.USERS_NOT_NULL)
            @Size(min = 1, message = ValidationMessages.USERS_SIZE)
            List<String> users
    ) {}

    record InviteUserInProjectResponseDto(
            String projectId
    ) {}

}
