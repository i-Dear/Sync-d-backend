package com.syncd.application.port.in.admin;

public interface CreateAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    CreateAdminResponseDto createAdmin(String email, String password, String name);

    // ======================================
    // DTO
    // ======================================
    record CreateAdminRequestDto(
            String email,
            String password,
            String name
    ) {}


    record CreateAdminResponseDto(
            String id
    ) {}
}