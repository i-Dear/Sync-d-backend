package com.syncd.application.port.in;

public interface GetRoomAuthTokenUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(String userId);

    GetRoomAuthTokenResponseDto Test(String uesrId, String roomId);

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}


    record TestDto(String roomId){}
    }
