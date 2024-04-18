package com.syncd.adapter.in.web;

import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import lombok.RequiredArgsConstructor;
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
    public CreateProjectResponseDto createProject(@RequestBody CreateProjectRequestDto requestDto){
        return createProjectUsecase.createProject(requestDto);
    }

    @PostMapping("/invite")
    public InviteUserInProjectResponseDto inviteUser(@RequestBody InviteUserInProjectRequestDto requestDto){
        return inviteUserInProjectUsecase.inviteUserInProject(requestDto);
    }

    @PostMapping("/withdraw")
    public WithdrawUserInProjectResponseDto withdrawUser(@RequestBody WithdrawUserInProjectRequestDto requestDto){
        return withdrawUserInProjectUsecase.withdrawUserInProject(requestDto);
    }

    @PostMapping("/delete")
    public DeleteProjectResponseDto deleteProject(@RequestBody DeleteProjectRequestDto requestDto){
        return deleteProjectUsecase.deleteProject(requestDto);
    }

    @PostMapping("/update")
    public UpdateProjectResponseDto deleteProject(@RequestBody UpdateProjectRequestDto requestDto){
        return updateProjectUsecase.updateProject(requestDto);
    }
}
