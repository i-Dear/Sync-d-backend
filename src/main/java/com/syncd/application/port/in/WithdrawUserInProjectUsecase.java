package com.syncd.application.port.in;
import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface WithdrawUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    WithdrawUserInProjectResponseDto withdrawUserInProject(String userId, String projectId, List<String> users);
    // ======================================
    // DTO
    // ======================================
    record WithdrawUserInProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            @NotNull(message = ValidationMessages.USERS_NOT_NULL)
            @Size(min = 1, message = ValidationMessages.USERS_SIZE)
            List<String> users
    ) {
    }
    record WithdrawUserInProjectResponseDto(
            String projectId
    ) {
    }
}
