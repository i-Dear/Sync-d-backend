package com.syncd.application.port.out.persistence.project.dto;

import lombok.Data;

import java.util.List;

public record ProjectDto(
        String id,
        String name,
        String description,
        List<UserRoleForProjectDto> users
){}
