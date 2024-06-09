package com.syncd.application.port.in.admin;

public interface LoginAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    LoginAdminUsecase.LoginResponseDto login(String email, String password);

    // ======================================
    // DTO
    // ======================================
    record LoginRequestDto(
        String email,
        String password
    ) {}


    record LoginResponseDto(
            String token
    ) {}
}