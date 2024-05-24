package com.syncd.application.port.in.admin;

import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.enums.UserAccountStatus;

import java.util.List;

public interface UpdateUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    UpdateUserResponseDto updateUser(String userId, String email,
                                     String name,
                                     UserAccountStatus status,
                                     String profileImg,
                                     List<String> projectIds);

    record UpdateUserRequestDto(
            String userId,
            String email,
            String name,
            UserAccountStatus status,
            String profileImg,
            List<String> projectIds
    ) {}


    record UpdateUserResponseDto(
            UserEntity userEntity
    ) {}
}
