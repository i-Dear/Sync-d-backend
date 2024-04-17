package com.syncd.application.service;

import com.syncd.adapter.out.liveblock.LiveblockApiAdapter;
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
public class ProjectService implements CreateProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase {
    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;

    private final LiveblockApiAdapter liveblockApiAdapter;


    @Override
    public CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto) {
        UserInProject user = UserInProject.builder()
                .userId(requestDto.userId())
                .role(Role.HOST)
                .build();

        Project project = Project.builder()
                .name(requestDto.name())
                .description(requestDto.description())
                .img(requestDto.img())
                .users(Arrays.asList(user))
                .build();

        return new CreateProjectResponseDto(writeProjectPort.CreateProject(project));
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(requestDto.userId());
        return mapProjectsToResponse(requestDto.userId(),projects);
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
        return UserRoleDto.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .projectId(projectId)
                .build();
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
}
