package com.syncd.application.port.in;

public interface CreateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectResponseDto createProject(String userId,String name, String description, String img);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectRequestDto(
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