package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;

import java.util.List;

public interface SearchUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    SearchUserAdminResponseDto searchUsers(String status, String searchType, String searchText);

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
