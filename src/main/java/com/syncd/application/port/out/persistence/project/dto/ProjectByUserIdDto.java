package com.syncd.application.port.out.persistence.project.dto;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;

import java.util.List;

public record ProjectByUserIdDto(
        String userId,
        String id,
        String name,
        String description,
        Role role,
        RoomPermission roomPermission
){}
