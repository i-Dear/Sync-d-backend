package com.syncd.mapper;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.out.gmail.SendMailPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    Project mapProjectEntityToProject(ProjectEntity projectEntity);

    ProjectEntity mapProjectToProjectEntity(Project project);

    List<Project> mapProjectEntitiesToProjects(List<ProjectEntity> projectEntities);

    ProjectForGetAllInfoAboutRoomsByUserIdResponseDto mapProjectToProjectForGetAllInfoDto(Project project);

    // ======================================
    // ProjectsToProjectIds
    // ======================================

    default List<String> mapProjectsToProjectIds(List<Project> projects) {
        return projects.stream()
                .map(this::mapProjectToProjectId)
                .collect(Collectors.toList());
    }

    default String mapProjectToProjectId(Project project) {
        return project.getId();
    }

    // ======================================
    // ProjectsToProjectForGetAllInfoDtos
    // ======================================

    default List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> mapProjectsToProjectForGetAllInfoDtos(String userId, List<Project> projects) {
        return projects.stream()
                .map(project -> convertProjectToProjectForGetAllInfoDto(userId, project))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    default ProjectForGetAllInfoAboutRoomsByUserIdResponseDto convertProjectToProjectForGetAllInfoDto(String userId, Project project) {
        Role role = determineUserRole(project.getUsers(), userId);
        List<String> userEmails = extractUserEmails(project.getUsers());

        return new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                project.getName(),
                project.getId(),
                project.getDescription(),
                role,
                userEmails,
                project.getProgress(),
                project.getLastModifiedDate()
        );
    }

    default Role determineUserRole(List<UserInProject> users, String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .map(UserInProject::getRole)
                .findFirst()
                .orElse(Role.MEMBER);
    }

    default List<String> extractUserEmails(List<UserInProject> users) {
        return users.stream()
                .map(UserInProject::getUserId)
                .collect(Collectors.toList());
    }

    // ======================================
    // ProjectsToGetAllRoomsByUserIdResponseDto
    // ======================================

    default GetAllRoomsByUserIdResponseDto mapProjectsToGetAllRoomsByUserIdResponseDto(String userId, List<Project> projects) {
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectDtos = mapProjectsToProjectForGetAllInfoDtos(userId, projects);
        return new GetAllRoomsByUserIdResponseDto(userId, projectDtos);
    }

    // ======================================
    // EmailsToUsersInProject
    // ======================================

    default List<UserInProject> mapEmailsToUsersInProject(List<String> userEmails, String hostName, String projectName, String projectId, ReadUserPort readUserPort, SendMailPort sendMailPort) {
        return userEmails.stream()
                .map(email -> createUserInProjectWithRoleMember(email, hostName, projectName, projectId, readUserPort, sendMailPort))
                .collect(Collectors.toList());
    }

    default UserInProject createUserInProjectWithRoleMember(String userEmail, String hostName, String projectName, String projectId, ReadUserPort readUserPort, SendMailPort sendMailPort) {
        User user = readUserPort.findByEmail(userEmail);
        sendMailPort.sendInviteMail(userEmail, hostName, user.getName(), projectName, projectId);
        return new UserInProject(user.getId(), Role.MEMBER);
    }




}
