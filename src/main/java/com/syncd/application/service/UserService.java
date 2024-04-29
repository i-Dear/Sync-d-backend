package com.syncd.application.service;

import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class UserService implements RegitsterUserUsecase {
    private final ReadUserPort readUserPort;
    private final WriteUserPort writeUserPort;
    private final AuthenticationPort authenticationPort;

    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerDto){
        String userId = writeUserPort.createUser(registerDto.name(), registerDto.email(),"").value();
        TokenDto tokens = authenticationPort.GetJwtTokens(new UserForTokenDto(userId));
        return new RegisterUserResponseDto(tokens.accessToken(), tokens.refreshToken());
    }

}
