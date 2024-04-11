package com.syncd.application.port.in;


public interface LoginUserUsecase {
    // ======================================
    // METHOD
    // ======================================
    LoginUserResponsetDto loginUser(LoginUserRequestDto requestDto);
    // ======================================
    // DTO
    // ======================================
    record LoginUserRequestDto(
            String email,
            String password
    ){}

    record LoginUserResponsetDto(
            String accessToken,
            String refreshToken
    ){}
}
