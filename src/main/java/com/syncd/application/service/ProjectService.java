package com.syncd.application.service;

import com.syncd.adapter.in.web.exception.exceptions.ProjectNotFoundException;
import com.syncd.adapter.in.web.exception.exceptions.ProjectOperationException;
import com.syncd.application.domain.project.Project;
import com.syncd.application.domain.project.ProjectMapper;
import com.syncd.application.port.in.*;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import com.syncd.application.port.out.persistence.project.dto.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, CreateProjectUsecase, DeleteProjectUsecase, WithdrawUserInProjectUsecase, InviteUserInProjectUsecase, UpdateProjectDetailUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        String token = liveblocksPort.GetRoomAuthToken();
        return new GetRoomAuthTokenResponseDto(token);
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        List<ProjectDto> allProjectDto = readProjectPort.findByUserId(requestDto.userId());
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
        CreateProjectResponseDto response = new CreateProjectResponseDto(writeProjectPort.CreateProject(requestDto.userId(),requestDto.projectName(),requestDto.projectDescription()).value());
        return response;
    }

    @Override
    public DeleteProjectResponseDto deleteProject(DeleteProjectRequestDto requestDto){
        try {
            String projectId = requestDto.projectId();
            ProjectId result = writeProjectPort.DeleteProject(projectId);
            return new DeleteProjectResponseDto(result.value());
        } catch (DataAccessException e) {
            throw new ProjectOperationException("해당 ID의 Project를 삭제하는 데 실패하였습니다.: " + requestDto.projectId());
        } catch (NoSuchElementException e) {
            throw new ProjectNotFoundException("해당 ProjectId를 가진 요소를 찾을 수 없습니다.: " + requestDto.projectId());
        }
    }

    @Override
    public WithdrawUserInProjectResponseDto withdrawUserInProject(WithdrawUserInProjectRequestDto requestDto){
        try {
            ProjectId result = writeProjectPort.WithdrawUserInProject(requestDto.projectId(), requestDto.users());
            return new WithdrawUserInProjectResponseDto(result.value());
        } catch (DataAccessException e) {
            throw new ProjectOperationException("해당 ProjectID에서 User를 추방 하는 것에 실패하였습니다.: " + requestDto.projectId());
        } catch (NoSuchElementException e) {
            throw new ProjectNotFoundException("해당 프로젝트 또는 유저를 찾을 수 없습니다.: " + requestDto.projectId());
        }
    }

    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(InviteUserInProjectRequestDto requestDto){
        try {
            ProjectId result = writeProjectPort.InviteUserInProject(requestDto.projectId(), requestDto.users());
            return new InviteUserInProjectResponseDto(result.value());
        } catch (DataAccessException e) {
            throw new ProjectOperationException("해당 Project에 유저를 초대하는 것에 실패하였습니다.: " + requestDto.projectId());
        }
    }

    @Override
    public UpdateProjectDetailResponseDto updateProjectDetail(UpdateProjectDetailRequestDto requestDto){
        try {
            ProjectId result = writeProjectPort.UpdateProjectDetails(requestDto.projectId(), requestDto.projectName(), requestDto.description(), requestDto.image());
            return new UpdateProjectDetailResponseDto(result.value());
        } catch (DataAccessException e) {
            throw new ProjectOperationException("해당 Project detail를 업데이트하는 것에 실패하였습니다.: " + requestDto.projectId());
        }
    }
}
