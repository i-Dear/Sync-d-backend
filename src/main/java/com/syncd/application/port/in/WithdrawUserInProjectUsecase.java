package com.syncd.application.port.in;
import java.util.List;

public interface WithdrawUserInProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    WithdrawUserInProjectResponseDto withdrawUserInProject(WithdrawUserInProjectRequestDto requestDto);
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
