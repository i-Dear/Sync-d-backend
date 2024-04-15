package com.syncd.application.service;

import com.syncd.application.domain.project.Project;
import com.syncd.application.domain.project.ProjectMapper;
import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.liveblock.dto.GetRoomAuthTokenDto;
import com.syncd.application.port.out.liveblock.dto.UserRoleForLiveblocksDto;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.project.dto.ProjectByUserIdDto;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import com.syncd.enums.RoomPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, CreateProjectUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadProjectPort readTeamPort;
    private final WriteProjectPort writeTeamPort;
    private final ProjectMapper projectMapper;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto) {
        // Example of fetching projects and converting them to roles. Adjust according to your actual logic.
        List<UserRoleForLiveblocksDto> roles = fetchProjects(getRoomAuthTokenRequestDto.userId()).stream()
                .map(project -> new UserRoleForLiveblocksDto(project.id(), project.role(), project.roomPermission()))
                .collect(Collectors.toList());
        // Now calling the Liveblocks API
        System.out.print(roles);
        GetRoomAuthTokenDto authTokenDto = liveblocksPort.GetRoomAuthToken(getRoomAuthTokenRequestDto.userId(), roles);
        System.out.print(authTokenDto);
        // Assuming the GetRoomAuthTokenDto returns a token directly. Adjust this based on the actual API.
        return new GetRoomAuthTokenResponseDto(authTokenDto.token());
    }

    @Override
    public GetRoomAuthTokenResponseDto Test(TestDto req) {
        // Example of fetching projects and converting them to roles. Adjust according to your actual logic.

        GetRoomAuthTokenDto authTokenDto = liveblocksPort.Test(req.userId(),req.roomId());
        // Assuming the GetRoomAuthTokenDto returns a token directly. Adjust this based on the actual API.
        return new GetRoomAuthTokenResponseDto(authTokenDto.token());

    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        List<ProjectByUserIdDto> allProjectDto = fetchProjects(requestDto.userId());
        List<Project> allProjects = convertToProjectEntities(allProjectDto);
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectsForResponse = mapToResponseDto(allProjects, requestDto.userId());

        return new GetAllRoomsByUserIdResponseDto(requestDto.userId(), projectsForResponse);
    }
    private List<Project> convertToProjectEntities(List<ProjectByUserIdDto> projectDtos) {
        return projectDtos.stream()
                .map(this::convertDtoToProject)
                .collect(Collectors.toList());
    }

    private Project convertDtoToProject(ProjectByUserIdDto dto) {
        // Assuming Project class has an all-args constructor or using @Builder pattern here
        Project project = Project.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .users(new ArrayList<>())  // starts with an empty list of users
                .build();

        // You might want to add some initial users or other setup here
        return project;
    }
    private List<ProjectByUserIdDto> fetchProjects(String userId) {
        List<ProjectByUserIdDto> projects = readTeamPort.findByUserId(userId);
        return projects;
    }

    private List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> mapToResponseDto(List<Project> projects, String userId) {
        return projects.stream()
                .map(project -> projectMapper.toProjectForGetAllInfoAboutRoomsByUserIdResponseDto(project, userId))
                .collect(Collectors.toList());
    }

    @Override
    public CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto){
        CreateProjectResponseDto response = new CreateProjectResponseDto(writeTeamPort.CreateProject(requestDto.userId(),requestDto.projectName(),requestDto.projectDescription()).value());
        return response;
    }

}
