package com.syncd.application.port.in;


public interface RegitsterUserUsecase {
    // ======================================
    // METHOD
    // ======================================
    RegisterUserResponseDto registerUser(RegisterUserRequestDto registerDto);
    // ======================================
    // DTO
    // ======================================

    record RegisterUserRequestDto(
            String name,
            String email,
            String password
    ){}

    record RegisterUserResponseDto(
            String accessToken,
            String refreshToken
    ){}
}
