package com.syncd.application.port.out.persistence.project;

import com.syncd.application.port.out.persistence.project.dto.ProjectId;
import com.syncd.application.port.out.persistence.project.dto.UserRoleForProjectDto;

import java.util.List;

public interface WriteProjectPort {
    ProjectId CreateProject(String hostId, String projectName, String projectDescription);

    ProjectId AddUsersToProject(String projectId, List<UserRoleForProjectDto> users);

    ProjectId UpdateUsersRoleToProject(String projectId, List<UserRoleForProjectDto> users);

    ProjectId UpdateProjectInfo(String projectId, String projectName, String projectDescription);

    ProjectId RemoveUserFromProject(String projectId, String userId);
}
