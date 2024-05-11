package com.syncd.application.port.in;

public interface SyncProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    SyncProjectResponseDto syncProject(String userId, String projectId, int projectStage);
    // ======================================
    // DTO
    // ======================================

    record SyncProjectRequestDto(
            String projectId,
            int projectStage
    ){}

    record SyncProjectResponseDto(
            String projectId
    ){}
}
