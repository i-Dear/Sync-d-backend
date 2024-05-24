package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;

import java.util.List;

public interface GetAllProjectAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetAllProjectResponseDto getAllProject();

    // ======================================
    // DTO
    // ======================================

    record GetAllProjectResponseDto(
            List<ProjectEntity> projectEntities
    ) {}
}
