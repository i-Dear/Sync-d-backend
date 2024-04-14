package com.syncd.application.port.out.persistence.project;

import com.syncd.application.port.out.persistence.project.dto.ProjectDto;

import java.util.List;

public interface ReadProjectPort {
    List<ProjectDto> findByUserId(String userId);


}
