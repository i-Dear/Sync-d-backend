package com.syncd.application.port.out.autentication.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
){}
