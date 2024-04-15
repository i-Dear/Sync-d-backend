package com.syncd.application.port.in;

public interface GetRoomAuthTokenUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto);

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}

    record GetRoomAuthTokenRequestDto(String userId){}
    }
