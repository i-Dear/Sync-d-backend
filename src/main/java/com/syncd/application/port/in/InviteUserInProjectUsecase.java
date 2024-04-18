package com.syncd.application.port.in;

import java.util.List;

public interface InviteUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    InviteUserInProjectResponseDto inviteUserInProject(InviteUserInProjectRequestDto requestDto);
    // ======================================
    // DTO
    // ======================================
    record InviteUserInProjectRequestDto(
            String userId,
            String projectId,
            List<String> users
    ) {}

    record InviteUserInProjectResponseDto(
            String projectId
    ) {}
}
