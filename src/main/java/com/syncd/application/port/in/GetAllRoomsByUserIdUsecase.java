package com.syncd.application.port.in;

import com.syncd.enums.Role;

import java.util.List;

public interface GetAllRoomsByUserIdUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetAllInfoAboutRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllInfoAboutRoomsByUserIdRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record GetAllInfoAboutRoomsByUserIdResponseDto(
            String userId,
            List<OrgForGetAllInfoAboutRoomsByUserIdResponseDto> organizations
    ){}

    record OrgForGetAllInfoAboutRoomsByUserIdResponseDto(
            String orgName,
            String orgId,
            String orgDescription,
            Role role,
            String roomId
    ){}

    record GetAllInfoAboutRoomsByUserIdRequestDto(String email){}

    }
