package com.syncd.application.port.in;

public interface UpdateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateProjectResponseDto updateProject(String userId,  String projectId,
                                           String projectName,
                                           String description,
                                           String image );

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectRequestDto(
            String projectId,
            String projectName,
            String description,
            String image

    ) {
    }

    record UpdateProjectResponseDto(
            String projectId
    ) {
    }

}
