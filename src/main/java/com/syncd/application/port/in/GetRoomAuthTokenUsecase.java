package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface GetRoomAuthTokenUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId
    );

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}


    record TestDto(
            @NotBlank(message=ValidationMessages.ROOM_ID_NOT_BLANK)
            String roomId){}
    }
