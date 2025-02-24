package com.syncd.application.port.in;

import com.syncd.enums.Role;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface GetAllRoomsByUserIdUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId
    );

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
            List<String> userEmails,
            int progress,
            String lastModifiedDate,
            String projectImg
    ){}

    record GetAllRoomsByUserIdRequestDto(
            @NotBlank(message = ValidationMessages.USER_ID_NOT_BLANK)
            String userId){}

    }
