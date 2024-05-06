package com.syncd.application.port.in;

import lombok.Data;

import java.util.List;

public interface GetUserInfoUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetUserInfoResponseDto getUserInfo(String userId);

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