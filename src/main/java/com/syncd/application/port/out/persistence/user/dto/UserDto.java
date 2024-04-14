package com.syncd.application.port.out.persistence.user.dto;


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