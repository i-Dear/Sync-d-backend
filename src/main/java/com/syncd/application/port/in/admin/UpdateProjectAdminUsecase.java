package com.syncd.application.port.in.admin;

import com.syncd.enums.Role;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpdateProjectAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    UpdateProjectAdminResponseDto updateProject(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId,
            String projectId,
            String name,
            String description,
            String img,
            List<UserInProjectRequestDto> users,
            int progress,
            int leftChanceForUserstory
    );

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectAdminRequestDto(
            String projectId,
            String name,
            String description,
            String img,
            List<UserInProjectRequestDto> users,
            int progress,
            int leftChanceForUserstory
    ) {}

    record UpdateProjectAdminResponseDto(
            String projectId
    ) {}

    record UserInProjectRequestDto(
            String userId,
            Role role
    ) {}

}
