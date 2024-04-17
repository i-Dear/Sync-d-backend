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
            String name,
            String description,
            String img
    ) {
    }

    record CreateProjectResponseDto(
            String projectId
    ) {
    }

}