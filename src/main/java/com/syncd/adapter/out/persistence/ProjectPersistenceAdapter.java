package com.syncd.adapter.out.persistence;

//import com.syncd.adapter.out.persistence.repository.organization.OrganizationDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionDao;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionEntity;
import com.syncd.adapter.out.persistence.repository.projectPermission.ProjectPermissionEntity.*;

import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.project.dto.ProjectByUserIdDto;
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
    public List<ProjectByUserIdDto> findByUserId(String userId){
        List<ProjectPermissionEntity> allPermissions = projectPermissionDao.findAllByUserId(userId);
        return allPermissions.stream()
                .map(permission -> createProjectByUserIdDtoFromPermission(permission))
                .collect(Collectors.toList());
    }
    private ProjectByUserIdDto createProjectByUserIdDtoFromPermission(ProjectPermissionEntity permission) {
        ProjectEntity project = projectDao.findById(permission.getProjectId()).orElse(null);

        if (project != null) {
            return new ProjectByUserIdDto(
                    permission.getUserId(),
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    permission.getRole(),
                    permission.getRoomPermission()
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

        addUsersToProject(projectEntity.getId(),userRoles);
        return new ProjectId(projectEntity.getId());
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
            ProjectPermissionEntity permissionEntity = ProjectPermissionEntity.builder()
                    .userId(user.userId())
                    .projectId(projectId)
                    .role(user.role())
                    .roomPermission(RoomPermission.WRITE)
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
            projectPermissionDao.findByProjectIdAndUserId(projectId, user.userId())
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
        projectPermissionDao.findByProjectIdAndUserId(projectId, userId)
                .ifPresent(projectPermission -> projectPermissionDao.delete(projectPermission));
        return new ProjectId(projectId);
    }

}