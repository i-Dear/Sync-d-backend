package com.syncd.application.port.out.persistence.project;

import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;

import java.util.List;

public interface ReadProjectPort {
    List<Project> findAllProjectByUserId(String userId);
    Project findProjectByProjectId(String projectId);

}
