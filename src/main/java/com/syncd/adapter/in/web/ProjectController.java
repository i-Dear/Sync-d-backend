package com.syncd.adapter.in.web;

import com.syncd.adapter.in.oauth.JwtTokenProvider;
import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.SyncProjectUsecase.*;
import com.syncd.dto.MakeUserStoryReauestDto;
import com.syncd.dto.MakeUserStoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final CreateProjectUsecase createProjectUsecase;

    private final InviteUserInProjectUsecase inviteUserInProjectUsecase;

    private final WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;

    private final DeleteProjectUsecase deleteProjectUsecase;

    private final UpdateProjectUsecase updateProjectUsecase;

    private final SyncProjectUsecase syncProjectUsecase;
    private final MakeUserstoryUsecase makeUserstoryUsecase;

    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/create")
    public CreateProjectResponseDto createProject(HttpServletRequest request, @Valid @RequestBody CreateProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return createProjectUsecase.createProject(jwtTokenProvider.getUserIdFromToken(token),jwtTokenProvider.getUsernameFromToken(token), requestDto.name(), requestDto.description(), requestDto.img(), requestDto.userEmails());
    }

    @PostMapping("/invite")
    public InviteUserInProjectResponseDto inviteUser(HttpServletRequest request, @Valid @RequestBody InviteUserInProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return inviteUserInProjectUsecase.inviteUserInProject(jwtTokenProvider.getUserIdFromToken(token),requestDto.projectId(),requestDto.users());
    }

    @PostMapping("/withdraw")
    public WithdrawUserInProjectResponseDto withdrawUser(HttpServletRequest request,@Valid @RequestBody WithdrawUserInProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return withdrawUserInProjectUsecase.withdrawUserInProject(jwtTokenProvider.getUserIdFromToken(token), requestDto.projectId(),requestDto.users());
    }

    @PostMapping("/delete")
    public DeleteProjectResponseDto deleteProject(HttpServletRequest request,@Valid @RequestBody DeleteProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return deleteProjectUsecase.deleteProject(jwtTokenProvider.getUserIdFromToken(token),requestDto.projectId());
    }

    @PostMapping("/update")
    public UpdateProjectResponseDto updateProject(HttpServletRequest request, @Valid @RequestBody UpdateProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return updateProjectUsecase.updateProject(jwtTokenProvider.getUserIdFromToken(token),requestDto.projectId(),requestDto.projectName(),requestDto.description(),requestDto.image());
    }

    @PostMapping("/sync")
    public SyncProjectResponseDto syncProject(HttpServletRequest request, @Valid @RequestBody SyncProjectRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        return syncProjectUsecase.syncProject(jwtTokenProvider.getUserIdFromToken(token), requestDto.projectId(), requestDto.projectStage());
    }

    @PostMapping("/userstory")
    public ResponseEntity<MakeUserStoryResponseDto> makeUserStory(HttpServletRequest request, @RequestBody MakeUserStoryReauestDto makeUserStoryReauestDto) {
        String token = jwtTokenProvider.resolveToken(request);
        MakeUserStoryResponseDto result = makeUserstoryUsecase.makeUserstory(jwtTokenProvider.getUserIdFromToken(token), makeUserStoryReauestDto.getProjectId(), makeUserStoryReauestDto.getScenario());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
