package com.syncd.application.port.in;

import com.syncd.enums.Role;

import java.util.List;

public interface CreateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectRequestDto(
            String userId,
            String projectName,
            String projectDescription
    ) {
    }

    record CreateProjectResponseDto(
            String projectId
    ) {
    }

}