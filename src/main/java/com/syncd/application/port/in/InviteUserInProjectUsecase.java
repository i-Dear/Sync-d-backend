package com.syncd.application.port.in;

import java.util.List;

public interface InviteUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    InviteUserInProjectResponseDto inviteUserInProject(String userId, String projectId, List<String> users);
    // ======================================
    // DTO
    // ======================================
    record InviteUserInProjectRequestDto(
            String projectId,
            List<String> users
    ) {}

    record InviteUserInProjectResponseDto(
            String projectId
    ) {}
}
