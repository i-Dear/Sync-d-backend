package com.syncd.application.port.in;
import java.util.List;

public interface WithdrawUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    WithdrawUserInProjectResponseDto withdrawUserInProject(String userId, String projectId, List<String> users);
    // ======================================
    // DTO
    // ======================================
    record WithdrawUserInProjectRequestDto(
            String projectId,
            List<String> users
    ) {
    }
    record WithdrawUserInProjectResponseDto(
            String projectId
    ) {
    }
}
