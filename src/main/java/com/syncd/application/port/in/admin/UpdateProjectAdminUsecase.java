package com.syncd.application.port.in.admin;

import com.syncd.enums.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpdateProjectAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    UpdateProjectAdminResponseDto updateProject(String projectId, String name, String description,
                                                String img,
                                                List<UserInProjectRequestDto> users,
                                                int progress,
                                                int leftChanceForUserstory);

    // ======================================
    // DTO
    // ======================================
    record UpdateProjectAdminRequestDto(
            String projectId,
            String name,
            String description,
            String img,
            List<UserInProjectRequestDto> users,
            int progress,
            int leftChanceForUserstory
    ) {}

    record UpdateProjectAdminResponseDto(
            String projectId
    ) {}

    record UserInProjectRequestDto(
            String userId,
            Role role
    ) {}

}
