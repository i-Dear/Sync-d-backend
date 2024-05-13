package com.syncd.application.service;

import com.syncd.application.port.in.*;
import com.syncd.application.port.out.gmail.SendMailPort;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import com.syncd.exceptions.ProjectAlreadyExistsException;
import com.syncd.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements CreateProjectUsecase, JoinProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, UpdateProjectUsecase, WithdrawUserInProjectUsecase, InviteUserInProjectUsecase, DeleteProjectUsecase, SyncProjectUsecase {
    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;
    private final ReadUserPort readUserPort;
    private final LiveblocksPort liveblocksPort;
    private final SendMailPort sendMailPort;

    @Override
    public CreateProjectResponseDto createProject(String hostId, String hostName, String projectName, String description, String img, List<String> userEmails){
        List<User> users = readUserPort.usersFromEmails(userEmails);
        sendMailPort.sendIviteMailBatch(hostName, projectName, users);
        Project project = new Project();
        project = project.createProjectDomain(projectName, description, img, hostId, users);
        return new CreateProjectResponseDto(writeProjectPort.CreateProject(project));
    }

    @Override
    public JoinProjectResponseDto joinProject(String userId, String projectId) {
        UserInProject userInProject = new UserInProject(userId, Role.MEMBER);
        Project project = readProjectPort.findProjectByProjectId(projectId);

        List<UserInProject> users = project.getUsers();
        users.add(userInProject);
        project.setUsers(users);

        writeProjectPort.UpdateProject(project);

        return new JoinProjectResponseDto(projectId);
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        GetAllRoomsByUserIdResponseDto responseDto =  mapProjectsToResponse(userId, projects);
        return responseDto;
    }


    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        List<String> projectIds = projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        User userInfo = readUserPort.findByUserId(userId);
        return new GetRoomAuthTokenResponseDto(liveblocksPort.GetRoomAuthToken(userId, userInfo.getName(), userInfo.getProfileImg(), projectIds).token());
    }

    @Override
    public GetRoomAuthTokenResponseDto Test(String userId, String roomId) {
        return new GetRoomAuthTokenResponseDto(liveblocksPort.Test(userId, roomId).token());
    }

    @Override
    public DeleteProjectResponseDto deleteProject(String userId, String projectId) {
        writeProjectPort.RemoveProject(projectId);
        return new DeleteProjectResponseDto(projectId);
    }


    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(String userId, String projectId, List<String> userEmails) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);

        User host = readUserPort.findByUserId(userId);
        List<UserInProject> users = userEmails.stream()
                .map(email -> createUserInProjectWithRoleMember(email, host.getName(), project.getName()))
                .collect(Collectors.toList());

        project.addUsers(users);

        writeProjectPort.UpdateProject(project);
        return new InviteUserInProjectResponseDto(projectId);
    }

    @Override
    public UpdateProjectResponseDto updateProject(String userId,  String projectId,
                                                  String projectName,
                                                  String description,
                                                  String image ){
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);

        project.updateProjectInfo(projectName,description, image);
        writeProjectPort.UpdateProject(project);
        return new UpdateProjectResponseDto(projectId);
    }

    @Override
    public WithdrawUserInProjectResponseDto withdrawUserInProject(String userId, String projectId, List<String> userIds) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);
        project.withdrawUsers(userIds);

        writeProjectPort.UpdateProject(project);
        return new WithdrawUserInProjectResponseDto(projectId);
    }

    @Override
    public SyncProjectResponseDto syncProject(String userId, String projectId, int projectStage) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        writeProjectPort.AddProgress(projectId, projectStage);
        writeProjectPort.updateLastModifiedDate(projectId);

        return new SyncProjectResponseDto(projectId);
    }

    // ======================================
    // private methods (implements)
    // ======================================

    private void checkHost(Project project, String userId){
        if(!project.getHost().equals(userId)){
            throw new ProjectAlreadyExistsException();
        }
    }
    private UserInProject createUserInProjectWithRoleMember(String userEmail, String hostName, String projectName) {
        // 여기에 사용자 생성 및 역할 부여 로직 추가
        User user = readUserPort.findByEmail(userEmail);
        sendMailPort.sendInviteMail(userEmail, hostName, user.getName(), projectName);
        return new UserInProject(user.getId(), Role.MEMBER);
    }

    private GetAllRoomsByUserIdResponseDto mapProjectsToResponse(String userId, List<Project> projects) {
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectDtos = projects.stream()
                .map(project -> convertProjectToDto(userId, project))
                .filter(dto -> dto != null)  // Ensure that only relevant projects are included
                .collect(Collectors.toList());

        return new GetAllRoomsByUserIdResponseDto(userId, projectDtos);
    }

    private ProjectForGetAllInfoAboutRoomsByUserIdResponseDto convertProjectToDto(String userId, Project project) {
        Role userRole = project.getUsers().stream()
                .filter(user -> userId.equals(user.getUserId()))
                .map(UserInProject::getRole)
                .findFirst()
                .orElse(null);

        if (userRole == null) return null;

        List<UserInProject> usersInProject = project.getUsers();

        List<String> userEmails = usersInProject.stream()
                .map(UserInProject::getUserId)
                .map(readUserPort::findByUserId)
                .filter(user -> user != null)
                .map(User::getEmail)
                .collect(Collectors.toList());

        return new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                project.getName(),
                project.getId(),
                project.getDescription(),
                userRole,
                userEmails,
                project.getProgress(),
                project.getLastModifiedDate()
        );
    }

    private UserRoleDto convertUserToUserRoleDto(String projectId, UserInProject user) {
        return new UserRoleDto(projectId, user.getUserId(), user.getRole());
    }

}
