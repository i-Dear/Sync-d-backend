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
    private GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;

    @PostMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenRequestDto getRoomAuthToken){
        return getRoomAuthTokenUsecase.getRoomAuthToken(getRoomAuthToken);
    }

    @PostMapping("/")
    public GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllInfoAboutRoomsByUserIdRequestDto getAllInfoAboutRoomsByUserIdRequestDto){
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(getAllInfoAboutRoomsByUserIdRequestDto);
    }
}
