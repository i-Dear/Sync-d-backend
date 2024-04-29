package com.syncd.application.port.in;

public interface DeleteProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    DeleteProjectResponseDto deleteProject(String userId, String projectId);

    // ======================================
    // DTO
    // ======================================
    record DeleteProjectResponseDto(
            String projectId
    ){

    }
     record DeleteProjectRequestDto (
             String projectId
    ) {
    }
}



