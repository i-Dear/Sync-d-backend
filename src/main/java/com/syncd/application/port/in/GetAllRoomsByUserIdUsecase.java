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
            List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projects
    ){}

    record ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
            String name,
            String id,
            String description,
            Role role,
            String roomId
    ){}

    record GetAllRoomsByUserIdRequestDto(String userId){}

    }
