package com.syncd.dto;


import com.syncd.enums.UserAccountStatus;

import java.util.List;

public record UserDto(

        String userId,
        String email,
        String password,
        String name,
        UserAccountStatus status,
        String profileImg,
        List<String> projectIds
) {}