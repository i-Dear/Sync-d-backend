package com.syncd.application.port.in;

import java.util.List;

public interface CreateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    CreateProjectResponseDto createProject(String userId,String name, String description, String img, List<String> users);

    // ======================================
    // DTO
    // ======================================
    record CreateProjectRequestDto(
            String name,
            String description,
            String img,
            List<String> users
    ) {
    }

    record CreateProjectResponseDto(
            String projectId
    ) {
    }

}