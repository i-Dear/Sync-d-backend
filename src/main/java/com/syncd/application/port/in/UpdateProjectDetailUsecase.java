package com.syncd.application.port.in;

public interface UpdateProjectDetailUsecase {
    // ======================================
    // METHOD
    // ======================================
    UpdateProjectDetailResponseDto updateProjectDetail(UpdateProjectDetailRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectDetailRequestDto(
            String projectId,
            String projectName,
            String description,
            String image

    ) {
    }

    record UpdateProjectDetailResponseDto(
            String projectId
    ) {
    }

}
