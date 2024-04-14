package com.syncd.application.port.out.persistence.project.dto;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;
import lombok.Data;

public record UserRoleForProjectDto(
        String userId,
        Role role,
        RoomPermission roomPermission
){}
