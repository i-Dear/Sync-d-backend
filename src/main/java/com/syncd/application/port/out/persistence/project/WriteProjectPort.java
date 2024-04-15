package com.syncd.application.port.out.persistence.project;

import com.syncd.application.domain.project.Project;
import com.syncd.application.port.out.persistence.project.dto.ProjectId;
import com.syncd.application.port.out.persistence.project.dto.UserRoleForProjectDto;

import java.util.List;

public interface WriteProjectPort {
    ProjectId CreateProject(String hostId, String projectName, String projectDescription);

    ProjectId AddUsersToProject(String projectId, List<UserRoleForProjectDto> users);

    ProjectId UpdateUsersRoleToProject(String projectId, List<UserRoleForProjectDto> users);

    ProjectId UpdateProjectInfo(String projectId, String projectName, String projectDescription);

    ProjectId RemoveUserFromProjectPermission(String projectId, String userId);

//    ProjectId DeleteProject(String projectId);

//    ProjectId WithdrawUserInProject(String projectId, List<String> userId);

//    ProjectId InviteUserInProject(String projectId, List<String> userId);

//    ProjectId UpdateProjectDetails(String projectId, String newName, String newDescription, String newImage);
}
