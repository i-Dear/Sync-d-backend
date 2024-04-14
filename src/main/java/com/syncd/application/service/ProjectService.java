package com.syncd.application.service;

import com.syncd.application.domain.project.Project;
import com.syncd.application.domain.project.ProjectMapper;
import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, CreateProjectUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadProjectPort readTeamPort;
    private final WriteProjectPort writeTeamPort;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        String token = liveblocksPort.GetRoomAuthToken();
        return new GetRoomAuthTokenResponseDto(token);
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        List<ProjectDto> allProjectDto = readTeamPort.findByUserId(requestDto.userId());
        System.out.print(allProjectDto);
        List<Project> allProject = allProjectDto.stream()
                .map(el -> ProjectMapper.INSTANCE.fromProjectDto(el))
                .collect(Collectors.toList());

        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectsForResponse =  allProject.stream()
                .map(el -> ProjectMapper.INSTANCE.toProjectForGetAllInfoAboutRoomsByUserIdResponseDto(el))
                .collect(Collectors.toList());

        GetAllRoomsByUserIdResponseDto response = new GetAllRoomsByUserIdResponseDto(requestDto.userId(),projectsForResponse);
        return response;
    }

    @Override
    public CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto){
        CreateProjectResponseDto response = new CreateProjectResponseDto(writeTeamPort.CreateProject(requestDto.userId(),requestDto.projectName(),requestDto.projectDescription()).value());
        return response;
    }

}
