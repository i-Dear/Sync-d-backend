package com.syncd.adapter.in.web;

import com.syncd.application.port.in.OrganizationUsecase;
import com.syncd.application.port.in.OrganizationUsecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/room")
public class RoomController {
    private OrganizationUsecase organizationUsecase;

    @PostMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenRequestDto getRoomAuthToken){
        return organizationUsecase.getRoomAuthToken(getRoomAuthToken);
    }

    @PostMapping("/")
    public GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllInfoAboutRoomsByUserIdRequestDto getAllInfoAboutRoomsByUserIdRequestDto){
        return organizationUsecase.getAllInfoAboutRoomsByUserId(getAllInfoAboutRoomsByUserIdRequestDto);
    }
}
