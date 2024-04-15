package com.syncd.application.port.out.liveblock.dto;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;

public record UserRoleForLiveblocksDto(
        String projectId,
        Role role,
        RoomPermission roomPermission
){}