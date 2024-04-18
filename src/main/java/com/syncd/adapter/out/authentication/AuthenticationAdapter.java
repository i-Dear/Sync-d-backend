package com.syncd.adapter.out.authentication;

import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAdapter implements AuthenticationPort {

    public TokenDto GetJwtTokens(UserForTokenDto user){
        // TODO: token 생성 로직 구현
        return new TokenDto("Baerer accessToken", "Baerer refreshToken");
    }
}
