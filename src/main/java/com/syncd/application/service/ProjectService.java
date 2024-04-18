package com.syncd.application.service;

import com.syncd.adapter.out.liveblock.LiveblockApiAdapter;
import com.syncd.adapter.out.persistence.exception.ProjectAlreadyExistsException;
import com.syncd.application.port.in.*;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.ProjectMapper;
import com.syncd.domain.project.UserInProject;
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


@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements CreateProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase,UpdateProjectUsecase,WithdrawUserInProjectUsecase,InviteUserInProjectUsecase,DeleteProjectUsecase {
    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;

    private final LiveblockApiAdapter liveblockApiAdapter;


    @Override
    public CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto) {
        List<UserInProject> users =Arrays.asList(new UserInProject(requestDto.userId(),Role.HOST));

        Project project = new Project(null);
        project.setImg(requestDto.img());
        project.setName(requestDto.name());
        project.setDescription(requestDto.description());
        project.setUsers(users);

        return new CreateProjectResponseDto(writeProjectPort.CreateProject(project));
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(requestDto.userId());
        GetAllRoomsByUserIdResponseDto responseDto =  mapProjectsToResponse(requestDto.userId(),projects);
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

    private List<UserRoleDto> mapUserRoleDto(String userId, List<Project> projects) {
        return projects.stream()
                .flatMap(project -> project.getUsers().stream()
                        .map(user -> convertUserToUserRoleDto(project.getId(), user)))
                .filter(userRoleDto -> userId.equals(userRoleDto.projectId()))
                .collect(Collectors.toList());
    }

    private UserRoleDto convertUserToUserRoleDto(String projectId, UserInProject user) {
        return new UserRoleDto(projectId,user.getUserId(),user.getRole());
    }


    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(getRoomAuthTokenRequestDto.userId());
        List<UserRoleDto> userRole = mapUserRoleDto(getRoomAuthTokenRequestDto.userId(), projects);
        return new GetRoomAuthTokenResponseDto(liveblockApiAdapter.GetRoomAuthToken(getRoomAuthTokenRequestDto.userId(),userRole).token());
    }

    @Override
    public GetRoomAuthTokenResponseDto Test(TestDto test) {
        return new GetRoomAuthTokenResponseDto(liveblockApiAdapter.Test(test.userId(),test.roomId()).token());
    }

    @Override
    public DeleteProjectResponseDto deleteProject(DeleteProjectRequestDto requestDto) {
        Project project = readProjectPort.findProjectByProjectId(requestDto.projectId());
        checkHost(project, requestDto.userId());
        writeProjectPort.RemoveProject(requestDto.projectId());
        return new DeleteProjectResponseDto(requestDto.projectId());
    }

    private void checkHost(Project project, String userId){
        if(project.getHost()!=userId){
            throw new ProjectAlreadyExistsException(project.getId());
        }
    }

    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(InviteUserInProjectRequestDto requestDto) {
        Project project = readProjectPort.findProjectByProjectId(requestDto.projectId());
        checkHost(project,requestDto.userId());
        List<UserInProject> users = requestDto.users().stream().map(userId -> new UserInProject(userId, Role.MEMBER))
                .collect(Collectors.toList());

        project.addUsers(users);

        String projectId = writeProjectPort.UpdateProject(project);
        return new InviteUserInProjectResponseDto(projectId);
    }

    @Override
    public UpdateProjectResponseDto updateProject(UpdateProjectRequestDto requestDto) {
        Project project = readProjectPort.findProjectByProjectId(requestDto.projectId());
        checkHost(project,requestDto.userId());
        project.setName(requestDto.projectName());
        project.setDescription(requestDto.description());
        project.setImg(requestDto.image());

        String projectId = writeProjectPort.UpdateProject(project);
        return new UpdateProjectResponseDto(projectId);
    }

    @Override
    public WithdrawUserInProjectResponseDto withdrawUserInProject(WithdrawUserInProjectRequestDto requestDto) {
        Project project = readProjectPort.findProjectByProjectId(requestDto.projectId());
        checkHost(project,requestDto.userId());
        project.withdrawUsers(requestDto.users());

        String projectId = writeProjectPort.UpdateProject(project);
        return new WithdrawUserInProjectResponseDto(projectId);
    }
}
