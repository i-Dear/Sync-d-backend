package com.syncd.adapter.out.persistence;

//import com.syncd.adapter.out.persistence.repository.organization.OrganizationDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionDao;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionEntity;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionEntity.*;

import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import com.syncd.application.port.out.persistence.project.dto.ProjectId;
import com.syncd.application.port.out.persistence.project.dto.UserRoleForProjectDto;
import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

//TODO
// 1. 규모가 커지면 Adapter 레벨에서도 CQRS 고려하기

@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ReadProjectPort, WriteProjectPort {

    private final ProjectDao projectDao;
    private final ProjectPermissionDao projectPermissionDao;

    // ======================================
    // READ
    // ======================================
    public List<ProjectDto> findByUserId(String userId){
        List<ProjectPermissionEntity> allPermissions = projectPermissionDao.findAllByKey_UserId(userId);
        System.out.print(allPermissions);
        return allPermissions.stream()
                .map(permission -> createProjectDtoFromPermission(permission))
                .collect(Collectors.toList());
    }
    private ProjectDto createProjectDtoFromPermission(ProjectPermissionEntity permission) {
        // Find the project associated with the permission
        ProjectEntity project = projectDao.findById(permission.getKey().getProjectId()).orElse(null);

        // If project is found, create a ProjectDto
        if (project != null) {
            return new ProjectDto(
                    project.getProjectId(),
                    project.getName(),
                    project.getDescription(),
                    List.of(new UserRoleForProjectDto(permission.getKey().getUserId(),permission.getRole(), permission.getRoomPermission()))
            );
        }
        return null;
    }
    // ======================================
    // WRITE
    // ======================================

    public ProjectId CreateProject(String hostId, String projectName, String projectDescription) {
        ProjectEntity projectEntity = createProject(projectName,projectDescription);

        UserRoleForProjectDto hostUserRole = new UserRoleForProjectDto(hostId, Role.HOST,RoomPermission.WRITE);
        List<UserRoleForProjectDto> userRoles = List.of(hostUserRole);

        addUsersToProject(projectEntity.getProjectId(),userRoles);
        return new ProjectId(projectEntity.getProjectId());
    }

    public ProjectId AddUsersToProject(String projectId, List<UserRoleForProjectDto> users){
        String savedProjectId = addUsersToProject(projectId,users);
        return new ProjectId(savedProjectId);
    }

    private ProjectEntity createProject(String projectName, String projectDescription) {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .name(projectName)
                .description(projectDescription)
                .build();
        projectEntity = projectDao.save(projectEntity);
        return projectEntity;
    }

    private String addUsersToProject(String projectId,  List<UserRoleForProjectDto> users){
        users.forEach(user -> {
            ProjectPermissionKey key = ProjectPermissionKey.builder().userId(user.userId()).projectId(projectId).build();
            ProjectPermissionEntity permissionEntity = ProjectPermissionEntity.builder()
                    .key(key)
                    .role(user.role())
                    .build();
            projectPermissionDao.save(permissionEntity);
        });
        return projectId;
    }

    public ProjectId UpdateUsersRoleToProject(String projectId, List<UserRoleForProjectDto> users){
        updateUsersRoleToProject(projectId, users);
        return new ProjectId(projectId);
    }

    private String updateUsersRoleToProject(String projectId, List<UserRoleForProjectDto> users){
        users.forEach(user -> {
            projectPermissionDao.findByKey_ProjectIdAndKey_UserId(projectId, user.userId())
                    .ifPresent(permission -> {
                        permission.setRole(user.role());
                        projectPermissionDao.save(permission);
                    });
        });
        return projectId;
    }

    public ProjectId UpdateProjectInfo(String projectId, String projectName, String projectDescription){
        projectDao.findById(projectId).ifPresent(project -> {
            project.setName(projectName);
            project.setDescription(projectDescription);
            projectDao.save(project);
        });
        return new ProjectId(projectId);
    }

    public ProjectId RemoveUserFromProject(String projectId,String userId){
        projectPermissionDao.findByKey_ProjectIdAndKey_UserId(projectId, userId)
                .ifPresent(projectPermission -> projectPermissionDao.delete(projectPermission));
        return new ProjectId(projectId);
    }

}