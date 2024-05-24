package com.syncd.application.service;

import com.syncd.application.port.in.*;
import com.syncd.application.port.out.gmail.SendMailPort;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.openai.ChatGPTPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.dto.LiveblocksTokenDto;
import com.syncd.dto.MakeUserStoryResponseDto;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import com.syncd.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements CreateProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, UpdateProjectUsecase, WithdrawUserInProjectUsecase, InviteUserInProjectUsecase, DeleteProjectUsecase, SyncProjectUsecase, MakeUserstoryUsecase,JoinProjectUsecase {

    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;
    private final ReadUserPort readUserPort;
    private final LiveblocksPort liveblocksPort;
    private final SendMailPort sendMailPort;
    private final ChatGPTPort chatGPTPort;
    private final S3Port s3Port;

    @Override
    public CreateProjectResponseDto createProject(String hostId, String hostName, String projectName, String description, MultipartFile img, List<String> userEmails){
        List<User> users = new ArrayList<>();
        if(userEmails!=null){
            users = readUserPort.usersFromEmails(userEmails);
        }
        String imgURL = "";
        if (img != null && !img.isEmpty()) {
            Optional<String> optionalImgURL = s3Port.uploadMultipartFileToS3(img, hostName, projectName);
            imgURL = optionalImgURL.orElseThrow(() -> new IllegalStateException("Failed to upload image to S3"));
        }

        Project project = new Project();
        project = project.createProjectDomain(projectName, description, imgURL, hostId);

        CreateProjectResponseDto createProjectResponseDto = new CreateProjectResponseDto(writeProjectPort.CreateProject(project));

        User host = readUserPort.findByUserId(hostId);
        List<UserInProject> members = new ArrayList<>();
        if (userEmails != null && !userEmails.isEmpty()) {
            members = userEmails.stream()
                    .map(email -> createUserInProjectWithRoleMember(email, host.getName(), projectName, createProjectResponseDto.projectId()))
                    .collect(Collectors.toList());
        }

        sendMailPort.sendIviteMailBatch(hostName, projectName, users,project.getId());
        return createProjectResponseDto;
    }

    @Override
    public JoinProjectUsecase.JoinProjectResponseDto joinProject(String userId, String projectId) {
        UserInProject userInProject = new UserInProject(userId, Role.MEMBER);
        Project project = readProjectPort.findProjectByProjectId(projectId);

        List<UserInProject> users = project.getUsers();
        users.add(userInProject);
        project.setUsers(users);

        writeProjectPort.UpdateProject(project);

        return new JoinProjectUsecase.JoinProjectResponseDto(projectId);
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
        String liveblocksTokenDto = liveblocksPort.GetRoomAuthToken(userId, userInfo.getName(), userInfo.getProfileImg(), projectIds).token();
        System.out.println(liveblocksTokenDto);
        return new GetRoomAuthTokenResponseDto(liveblocksTokenDto);
    }

    @Override
    public GetRoomAuthTokenResponseDto Test(String userId, String roomId) {
        return new GetRoomAuthTokenResponseDto(liveblocksPort.Test(userId, roomId).token());
    }

    @Override
    public DeleteProjectResponseDto deleteProject(String userId, String projectId) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        if (project == null) {
            throw new CustomException(ErrorInfo.PROJECT_NOT_FOUND, "Project ID: " + projectId);
            // ... 코드 생략 ...
        }

        String imgFileName = project.getImgFileName();

        if (imgFileName != null) {
            Optional<Boolean> deletionResult = s3Port.deleteFileFromS3(imgFileName);
        }

        writeProjectPort.RemoveProject(projectId);
        return new DeleteProjectResponseDto(projectId);
    }


    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(String userId, String projectId, List<String> userEmails) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);

        User host = readUserPort.findByUserId(userId);
        List<UserInProject> users = userEmails.stream()
                .map(email -> createUserInProjectWithRoleMember(email, host.getName(), project.getName(), projectId))
                .collect(Collectors.toList());

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
    @Override
    @Transactional
    public MakeUserStoryResponseDto makeUserstory(String userId, String projectId, List<String> senarios){
        Project project = readProjectPort.findProjectByProjectId(projectId);
        if(project.getLeftChanceForUserstory() < 1){
            throw new CustomException(ErrorInfo.NOT_LEFT_CHANCE, "project id" +  projectId);
        }
        System.out.println(userId);
        boolean containsUserIdA = project.getUsers().stream()
                .anyMatch(user -> user.getUserId().equals(userId));

        if(!containsUserIdA){
            throw new CustomException(ErrorInfo.NOT_INCLUDE_PROJECT, "project id" +  projectId);
        }
        project.subLeftChanceForUserstory();
        writeProjectPort.UpdateProject(project);
        System.out.println(senarios);
        return chatGPTPort.makeUserstory(senarios);
    }

    // ======================================
    // private methods (implements)
    // ======================================

    private void checkHost(Project project, String userId){
        if(!project.getHost().equals(userId)){
            throw new CustomException(ErrorInfo.PROJECT_ALREADY_EXISTS, "project id" +  project.getId());
        }
    }
    private UserInProject createUserInProjectWithRoleMember(String userEmail, String hostName, String projectName,String projectId) {
        // 여기에 사용자 생성 및 역할 부여 로직 추가
        User user = readUserPort.findByEmail(userEmail);
        sendMailPort.sendInviteMail(userEmail, hostName, user.getName(), projectName,projectId);
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
