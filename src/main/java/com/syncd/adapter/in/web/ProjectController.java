package com.syncd.adapter.in.web;

import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdRequestDto;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.GetRoomAuthTokenRequestDto;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.GetRoomAuthTokenResponseDto;
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


    @PostMapping("/")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllRoomsByUserIdRequestDto requestDto){
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(requestDto);

    }

    @PostMapping("/create")
    public CreateProjectResponseDto createProject(@RequestBody CreateProjectRequestDto requestDto){
        return createProjectUsecase.createProject(requestDto);
    }


}
