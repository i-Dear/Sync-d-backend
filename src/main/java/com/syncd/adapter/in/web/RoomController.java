package com.syncd.adapter.in.web;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/room")
public class RoomController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;

    @PostMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenRequestDto getRoomAuthToken){
        return getRoomAuthTokenUsecase.getRoomAuthToken(getRoomAuthToken);

    }

    @PostMapping("/")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllRoomsByUserIdRequestDto requestDto){

        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(requestDto);

    }

    @PostMapping("/test-auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenUsecase.TestDto getRoomAuthToken){
        return getRoomAuthTokenUsecase.Test(getRoomAuthToken);

    }
}
