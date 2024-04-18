package com.syncd.dto;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;
import lombok.Builder;
import lombok.Data;

public record UserRoleDto(
        String projectId,
        String userId,
        Role role
){}
