package com.syncd.dto;

import com.syncd.enums.UserAccountStatus;

import java.util.List;
public record UserDto(
        String id,
        String email,
        String name,
        UserAccountStatus status,
        String profileImg,
        List<String> projectIds
) {}