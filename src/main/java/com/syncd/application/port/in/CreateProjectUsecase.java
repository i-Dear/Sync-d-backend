package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectResponseDto createProject(String hostId, String hostName, String name, String description, MultipartFile img, List<String> userEmails);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectRequestDto(
            @NotBlank(message = ValidationMessages.NAME_NOT_BLANK)
            String name,
            @NotBlank(message = ValidationMessages.DESCRIPTION_NOT_BLANK)
            String description,
            MultipartFile img,
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