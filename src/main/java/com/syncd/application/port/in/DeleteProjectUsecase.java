package com.syncd.application.port.in;

public interface DeleteProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    DeleteProjectResponseDto deleteProject(DeleteProjectRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record DeleteProjectResponseDto(
            String projectId
    ){

    }
     record DeleteProjectRequestDto (
             String userId,
             String projectId
    ) {
    }
}



