package com.syncd.application.service;

import com.syncd.application.domain.user.User;
import com.syncd.application.domain.user.UserMapper;
import com.syncd.application.port.in.LoginUserUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.autentication.dto.TokenDto;
import com.syncd.application.port.out.autentication.dto.UserForTokenDto;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class UserService implements RegitsterUserUsecase, LoginUserUsecase {
    private final ReadUserPort readUserPort;
    private final WriteUserPort writeUserPort;
    private final AuthenticationPort authenticationPort;

    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerDto){
        String userId = writeUserPort.createUser(registerDto.name(), registerDto.email(), registerDto.password()).value();
        TokenDto tokens = authenticationPort.GetJwtTokens(new UserForTokenDto(userId));
        return new RegisterUserResponseDto(tokens.accessToken(), tokens.refreshToken());
    }

    public  LoginUserResponsetDto loginUser(LoginUserRequestDto requestDto){
        String userId = readUserPort.findByEmailAndPassword(requestDto.email(), requestDto.password()).userId();
        TokenDto tokens = authenticationPort.GetJwtTokens(new UserForTokenDto(userId));
        return new LoginUserResponsetDto(tokens.accessToken(), tokens.refreshToken());
    }
}
