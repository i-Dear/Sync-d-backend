package com.syncd.adapter.in.web;

import com.syncd.application.port.in.GetUserInfoUsecase;
import com.syncd.application.port.in.UpdateUserInfoUsecase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.syncd.application.service.JwtService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final GetUserInfoUsecase getUserInfoUsecase;
    private final UpdateUserInfoUsecase updateUserInfoUsecase;
    private final JwtService jwtService;

//    @PostMapping("/register")
//    public RegisterUserResponseDto registerUser(@RequestBody RegisterUserRequestDto requestDto){
//        return regitsterUserUsecase.registerUser(requestDto);
//    }
    @GetMapping("/info")
    public GetUserInfoUsecase.GetUserInfoResponseDto getUserInfo(HttpServletRequest request){

        String token = jwtService.resolveToken(request);
        return getUserInfoUsecase.getUserInfo(jwtService.getUserIdFromToken(token));
    }

    @PostMapping("/update")
    public UpdateUserInfoUsecase.UpdateUserInfoResponseDto updateUserInfo(HttpServletRequest request, @Valid @ModelAttribute UpdateUserInfoUsecase.UpdateUserInfoRequestDto requestDto){

        String token = jwtService.resolveToken(request);
        return updateUserInfoUsecase.updateUserInfo(jwtService.getUserIdFromToken(token), requestDto.name(), requestDto.img());
    }
    
}
