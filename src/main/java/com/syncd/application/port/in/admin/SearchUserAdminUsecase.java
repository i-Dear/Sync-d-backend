package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface SearchUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    SearchUserAdminResponseDto searchUsers(
            String adminId,
            String status, String searchType, String searchText
    );

    // ======================================
    // DTO
    // ======================================
    record UserWithProjectsDto(
            UserEntity user,
            List<ProjectEntity> projects
    ) {}

    record SearchUserAdminResponseDto(
            List<UserWithProjectsDto> users
    ) {}
}
