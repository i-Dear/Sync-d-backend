package com.syncd.application.port.in;

import com.syncd.exceptions.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public interface GetUserInfoUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetUserInfoResponseDto getUserInfo(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId
    );

    // ======================================
    // DTO
    // ======================================

    record GetUserInfoResponseDto(

            String userId,
            String name,
            String img,
            String email,

            List<GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projects
    ) {
    }


}