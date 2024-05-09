package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface GetRoomAuthTokenUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId
    );

    GetRoomAuthTokenResponseDto Test(String uesrId, String roomId);

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}


    record TestDto(
            @NotBlank(message=ValidationMessages.ROOM_ID_NOT_BLANK)
            String roomId){}
    }
