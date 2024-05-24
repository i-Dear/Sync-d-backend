package com.syncd.application.port.in.admin;

import com.syncd.enums.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateProjectAdminUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectAdminResponseDto createProject(String name, String description, String img, List<UserInProjectRequestDto> users, int progress, int leftChanceForUserstory);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectAdminRequestDto(
            String name,
            String description,
            String img,
            List<UserInProjectRequestDto> users,
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
