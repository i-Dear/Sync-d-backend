package com.syncd.application.port.in.admin;

public interface DeleteProjectAdminUsecase {
    // ======================================
    // METHOD
    // ======================================
    DeleteProjectAdminResponseDto deleteProject(String projectId);

    // ======================================
    // DTO
    // ======================================
    record DeleteProjectAdminResponseDto(
            String projectId
    ){

    }
    record DeleteProjectAdminRequestDto (
            String projectId
    ) {
    }
}
