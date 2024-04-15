package com.syncd.application.port.in;

public interface GetRoomAuthTokenUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto);

    GetRoomAuthTokenResponseDto Test(TestDto test);

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}

    record GetRoomAuthTokenRequestDto(String userId){}


    record TestDto(String userId, String roomId){}

    }
