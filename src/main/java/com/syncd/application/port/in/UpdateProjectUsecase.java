package com.syncd.application.port.in;

public interface UpdateProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateProjectResponseDto updateProject(UpdateProjectRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectRequestDto(
            String userId,
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
