package com.syncd.application.port.in.admin;

public interface DeleteUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    DeleteUserResponseDto deleteUser(String userId);

    // ======================================
    // DTO
    // ======================================
    record DeleteUserRequestDto(
            String userId
    ) {}


    record DeleteUserResponseDto(
            String userId
    ) {}
}
