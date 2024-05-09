package com.syncd.adapter.in.web;

import com.syncd.adapter.in.oauth.JwtTokenProvider;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/room")
public class RoomController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        return getRoomAuthTokenUsecase.getRoomAuthToken(jwtTokenProvider.getUserIdFromToken(token));
    }

    @GetMapping("")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(jwtTokenProvider.getUserIdFromToken(token));
    }

    @PostMapping("/test-auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody @Valid GetRoomAuthTokenUsecase.TestDto getRoomAuthToken, HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        return getRoomAuthTokenUsecase.Test(jwtTokenProvider.getUserIdFromToken(token),getRoomAuthToken.roomId());

    }
}
