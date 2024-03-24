package com.syncd.application.port.in;

import com.syncd.enums.Role;

import java.util.List;

public interface OrganizationUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto);

    GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(GetAllInfoAboutRoomsByUserIdRequestDto requestDto);

    // ======================================
    // DTO
    // ======================================
    record GetRoomAuthTokenResponseDto(String token){}

    record GetRoomAuthTokenRequestDto(String userId, String roomId){}

    record GetAllInfoAboutRoomsByUserIdResponseDto(
            String userId,
            List<OrganizationInDto> organizations
    ){}

    record OrganizationInDto(
            String orgName,
            String orgId,
            String orgDescription,
            Role role,
            String roomId
    ){}

    record GetAllInfoAboutRoomsByUserIdRequestDto(String email){}

    }
