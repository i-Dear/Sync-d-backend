package com.syncd.application.port.in;

import com.syncd.enums.Role;

import java.util.List;

public interface GetAllRoomsByUserIdUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record GetAllRoomsByUserIdResponseDto(
            String userId,
            List<TeamForGetAllInfoAboutRoomsByUserIdResponseDto> teams
    ){}

    record TeamForGetAllInfoAboutRoomsByUserIdResponseDto(
            String teamName,
            String teamId,
            String teamDescription,
            Role role,
            String roomId
    ){}

    record GetAllRoomsByUserIdRequestDto(String email){}

    }
