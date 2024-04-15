package com.syncd.application.port.in;

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