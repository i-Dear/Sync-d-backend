package com.syncd.adapter.in.web;

import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectDetailUsecase;
import com.syncd.application.port.in.UpdateProjectDetailUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdRequestDto;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.GetRoomAuthTokenRequestDto;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.GetRoomAuthTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;
    private final CreateProjectUsecase createProjectUsecase;
    private final DeleteProjectUsecase deleteProjectUsecase;
    private final WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;
    private final InviteUserInProjectUsecase inviteUserInProjectUsecase;
    private final UpdateProjectDetailUsecase updateProjectDetailUsecase;

    @PostMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenRequestDto getRoomAuthToken){
        return getRoomAuthTokenUsecase.getRoomAuthToken(getRoomAuthToken);
    }

    @PostMapping("/")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllRoomsByUserIdRequestDto requestDto){
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(requestDto);

    }

    @PostMapping("/create")
    public CreateProjectResponseDto createProject(CreateProjectRequestDto requestDto){
        return createProjectUsecase.createProject(requestDto);
    }

    @PostMapping("/delete")
    public DeleteProjectResponseDto deleteProject(@RequestBody DeleteProjectRequestDto requestDto){
        return deleteProjectUsecase.deleteProject(requestDto);
    }

    @PostMapping("/withdraw")
    public WithdrawUserInProjectResponseDto withdrawUserInProject(@RequestBody  WithdrawUserInProjectRequestDto requestDto){
        return withdrawUserInProjectUsecase.withdrawUserInProject(requestDto);
    }

    @PostMapping("/invite")
    public InviteUserInProjectResponseDto inviteUserInProject(@RequestBody InviteUserInProjectRequestDto requestDto){
        return inviteUserInProjectUsecase.inviteUserInProject(requestDto);
    }

    @PostMapping("/update")
    public UpdateProjectDetailResponseDto updateProjectDetail(@RequestBody UpdateProjectDetailRequestDto requestDto){
        return updateProjectDetailUsecase.updateProjectDetail(requestDto);
    }
}
