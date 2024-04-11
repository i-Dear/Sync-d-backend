package com.syncd.adapter.in.web;

import com.syncd.application.port.in.LoginUserUsecase;
import com.syncd.application.port.in.LoginUserUsecase.*;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final RegitsterUserUsecase regitsterUserUsecase;
    private final LoginUserUsecase loginUserUsecase;

    @PostMapping("/register")
    public RegisterUserResponseDto registerUser(@RequestBody RegisterUserRequestDto requestDto){
        return regitsterUserUsecase.registerUser(requestDto);
    }

    @PostMapping("/login")
    public LoginUserResponsetDto loginUser(@RequestBody LoginUserRequestDto requestDto){
        return loginUserUsecase.loginUser(requestDto);
    }
}
