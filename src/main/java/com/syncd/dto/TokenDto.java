package com.syncd.dto;

import lombok.Data;

public record TokenDto(
        String accessToken,
        String refreshToken
){}
