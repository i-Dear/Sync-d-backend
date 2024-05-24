package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.user.UserEntity;

import java.util.List;

public interface GetAllUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetAllUserResponseDto getAllUser();


    // ======================================
    // DTO
    // ======================================

    record GetAllUserResponseDto(
            List<UserEntity> userEntities
    ) {}
}
