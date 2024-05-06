package com.syncd.adapter.in.web;

import com.syncd.application.port.in.GetUserInfoUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.syncd.adapter.in.oauth.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final GetUserInfoUsecase getUserInfoUsecase;
    private final JwtTokenProvider jwtTokenProvider;

//    @PostMapping("/register")
//    public RegisterUserResponseDto registerUser(@RequestBody RegisterUserRequestDto requestDto){
//        return regitsterUserUsecase.registerUser(requestDto);
//    }
    @GetMapping("/userinfo")
    public GetUserInfoUsecase.GetUserInfoResponseDto getUserInfo(HttpServletRequest request){

        String token = jwtTokenProvider.resolveToken(request);
        return getUserInfoUsecase.getUserInfo(jwtTokenProvider.getUserIdFromToken(token));
    }
}
