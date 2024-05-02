package com.syncd.application.service;

import com.syncd.adapter.out.liveblock.LiveblockApiAdapter;
import com.syncd.adapter.out.persistence.exception.ProjectAlreadyExistsException;
import com.syncd.application.port.in.*;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.ProjectMapper;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements CreateProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase,UpdateProjectUsecase,WithdrawUserInProjectUsecase,InviteUserInProjectUsecase,DeleteProjectUsecase {
    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;

    private final ReadUserPort readUserPort;
    private final LiveblocksPort liveblocksPort;


    @Override
    public CreateProjectResponseDto createProject(String userId,String name, String description, String img, List<String> userIds){
        List<UserInProject> users = Stream.concat(
                Stream.of(new UserInProject(userId, Role.HOST)), // 호스트 사용자
                userIds.stream().map(el -> new UserInProject(el, Role.MEMBER))
        ).collect(Collectors.toList());

        Project project = new Project(null);
        project.setImg(img);
        project.setName(name);
        project.setDescription(description);
        project.setUsers(users);

        return new CreateProjectResponseDto(writeProjectPort.CreateProject(project));
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        GetAllRoomsByUserIdResponseDto responseDto =  mapProjectsToResponse(userId,projects);
        return responseDto;
    }

    private GetAllRoomsByUserIdResponseDto mapProjectsToResponse(String userId, List<Project> projects) {
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectDtos = projects.stream()
                .map(project -> convertProjectToDto(userId, project))
                .filter(dto -> dto != null)  // Ensure that only relevant projects are included
                .collect(Collectors.toList());

        return new GetAllRoomsByUserIdResponseDto(userId, projectDtos);
    }

    private ProjectForGetAllInfoAboutRoomsByUserIdResponseDto convertProjectToDto(String userId, Project project) {
        // Find the user's role in this project
        Role userRole = project.getUsers().stream()
                .filter(user -> userId.equals(user.getUserId()))
                .map(UserInProject::getRole)
                .findFirst()
                .orElse(null);  // Default to null if the user is not found

        if (userRole == null) return null;  // If the user does not have a role in this project, skip it

        // Create the DTO
        return new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                project.getName(),
                project.getId(),
                project.getDescription(),
                userRole
        );
    }

//    private List<UserRoleDto> mapUserRoleDto(String userId, List<Project> projects) {
//        return projects.stream()
//                .flatMap(project -> project.getUsers().stream()
//                        .map(user -> convertUserToUserRoleDto(project.getId(), user)))
//                .filter(userRoleDto -> userId.equals(userRoleDto.projectId()))
//                .collect(Collectors.toList());
//    }

    private UserRoleDto convertUserToUserRoleDto(String projectId, UserInProject user) {
        return new UserRoleDto(projectId,user.getUserId(),user.getRole());
    }

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        List<String> projectIds = projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        User userInfo = readUserPort.findByUserId(userId);
        return new GetRoomAuthTokenResponseDto(liveblocksPort.GetRoomAuthToken(userId, userInfo.getName(),userInfo.getProfileImg(),projectIds).token());
    }

    @Override
    public GetRoomAuthTokenResponseDto Test(String uesrId, String roomId) {
        return new GetRoomAuthTokenResponseDto(liveblocksPort.Test(uesrId,roomId).token());
    }

    @Override
    public DeleteProjectResponseDto deleteProject(String userId, String projectId) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);
        writeProjectPort.RemoveProject(projectId);
        return new DeleteProjectResponseDto(projectId);
    }

    private void checkHost(Project project, String userId){
        if(project.getHost()!=userId){
            throw new ProjectAlreadyExistsException(project.getId());
        }
    }

    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(String userId, String projectId, List<String> userIds) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project,userId);
        List<UserInProject> users = userIds.stream().map(el -> new UserInProject(el, Role.MEMBER))
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
        checkHost(project,userId);
        project.setName(projectName);
        project.setDescription(description);
        project.setImg(image);

        writeProjectPort.UpdateProject(project);
        return new UpdateProjectResponseDto(projectId);
    }

    @Override
    public WithdrawUserInProjectResponseDto withdrawUserInProject(String userId, String projectId, List<String> userIds) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project,userId);
        project.withdrawUsers(userIds);

        writeProjectPort.UpdateProject(project);
        return new WithdrawUserInProjectResponseDto(projectId);
    }
}
