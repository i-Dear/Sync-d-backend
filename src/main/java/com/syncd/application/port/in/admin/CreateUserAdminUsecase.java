package com.syncd.application.port.in.admin;

import com.syncd.enums.UserAccountStatus;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateUserAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    CreateUserResponseDto addUser(
            String adminId,
            String email, String name, String status, MultipartFile profileImg,String projectIdsJson);

    // ======================================
    // DTO
    // ======================================
    record CreateUserRequestDto(
            String email,
            String name,
            String status,
            MultipartFile profileImg,
            String projectIdsJson
    ) {}


    record CreateUserResponseDto(
            String userId
    ) {}
}
