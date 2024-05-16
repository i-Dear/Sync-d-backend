package com.syncd.adapter.in.web;

import com.syncd.application.service.JwtService;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/room")
public class RoomController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    private final GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;
    private final JwtService jwtService;
    @GetMapping("/auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(HttpServletRequest request){
        String token = jwtService.resolveToken(request);
        return getRoomAuthTokenUsecase.getRoomAuthToken(jwtService.getUserIdFromToken(token));
    }

    @GetMapping("")
    public GetAllRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(HttpServletRequest request){
        String token = jwtService.resolveToken(request);
        return getAllRoomsByUserIdUsecase.getAllRoomsByUserId(jwtService.getUserIdFromToken(token));
    }

    @PostMapping("/test-auth")
    public GetRoomAuthTokenResponseDto getRoomAuthToken(@RequestBody @Valid GetRoomAuthTokenUsecase.TestDto getRoomAuthToken, HttpServletRequest request){
        String token = jwtService.resolveToken(request);
        return getRoomAuthTokenUsecase.Test(jwtService.getUserIdFromToken(token),getRoomAuthToken.roomId());

    }
}
