package com.syncd.application.port.in.admin;

import com.syncd.enums.UserAccountStatus;

import java.util.List;

public interface CreateUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    CreateUserResponseDto addUser(String email, String name, UserAccountStatus status, String profileImg, List<String> projectIds);

    // ======================================
    // DTO
    // ======================================
    record CreateUserRequestDto(
            String email,
            String name,
            UserAccountStatus status,
            String profileImg,
            List<String> projectIds
    ) {}


    record CreateUserResponseDto(
            String userId
    ) {}
}
