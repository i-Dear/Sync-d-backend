package com.syncd.adapter.in.web;

import com.syncd.adapter.in.oauth.PrincipalDetails;
import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public CreateProjectResponseDto createProject(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody CreateProjectRequestDto requestDto){
        return createProjectUsecase.createProject(principalDetails.getUser().getId(),requestDto.name(),requestDto.description(),requestDto.img(), requestDto.users());
    }

    @PostMapping("/invite")
    public InviteUserInProjectResponseDto inviteUser(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody InviteUserInProjectRequestDto requestDto){
        return inviteUserInProjectUsecase.inviteUserInProject(principalDetails.getUser().getId(),requestDto.projectId(),requestDto.users());
    }

    @PostMapping("/withdraw")
    public WithdrawUserInProjectResponseDto withdrawUser(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody WithdrawUserInProjectRequestDto requestDto){
        return withdrawUserInProjectUsecase.withdrawUserInProject(principalDetails.getUser().getId(), requestDto.projectId(),requestDto.users());
    }

    @PostMapping("/delete")
    public DeleteProjectResponseDto deleteProject(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody DeleteProjectRequestDto requestDto){
        return deleteProjectUsecase.deleteProject(principalDetails.getUser().getId(),requestDto.projectId());
    }

    @PostMapping("/update")
    public UpdateProjectResponseDto updateProject(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody UpdateProjectRequestDto requestDto){
        return updateProjectUsecase.updateProject(principalDetails.getUser().getId(),requestDto.projectId(),requestDto.projectName(),requestDto.description(),requestDto.image());
    }
}
