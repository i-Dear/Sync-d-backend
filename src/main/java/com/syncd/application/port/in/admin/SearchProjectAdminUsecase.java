package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public interface SearchProjectAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    SearchProjectAdminResponseDto searchProjects(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId,
            String name, String userId, Integer leftChanceForUserstory,
            String startDate, String endDate, Integer progress, int page, int pageSize
    );

    // ======================================
    // Search DTO
    // ======================================
    record SearchProjectAdminRequestDto(
            String name,
            String userId,
            Integer leftChanceForUserstory,
            String startDate,
            String endDate,
            Integer progress,
            int page,
            int pageSize
    ) {}

    record SearchProjectAdminResponseDto(
            List<ProjectEntity> projects,
            long totalCount,
            Map<String, UserEntity> userMap // 사용자 정보를 포함하는 맵 추가
    ) {}
}
