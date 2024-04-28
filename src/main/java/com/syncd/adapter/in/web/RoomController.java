package com.syncd.adapter.in.web;

import com.syncd.adapter.in.oauth.PrincipalDetails;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/room")
public class RoomController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;

    @GetMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return getRoomAuthTokenUsecase.getRoomAuthToken(principalDetails.getUser().getId());
    }

    @GetMapping("")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(principalDetails.getUser().getId());
    }

    @PostMapping("/test-auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody GetRoomAuthTokenUsecase.TestDto getRoomAuthToken,@AuthenticationPrincipal PrincipalDetails principalDetails){
        return getRoomAuthTokenUsecase.Test(principalDetails.getUser().getId(),getRoomAuthToken.roomId());

    }
}
