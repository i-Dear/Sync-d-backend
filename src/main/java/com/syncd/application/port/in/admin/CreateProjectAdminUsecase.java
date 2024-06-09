package com.syncd.application.port.in.admin;

import com.syncd.enums.Role;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateProjectAdminUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectAdminResponseDto createProject(
            String adminId,
            String name, String description, MultipartFile img,
            String usersJson,
            int progress, int leftChanceForUserstory
    );

    // ======================================
    // DTO
    // ======================================
    record CreateProjectAdminRequestDto(
            String name,
            String description,
            MultipartFile img,
            String usersJson,
            int progress,
            int leftChanceForUserstory
    ) {}

    record CreateProjectAdminResponseDto(
            String projectId
    ) {}

    record UserInProjectRequestDto(
            String userId,
            Role role
    ) {}
}
