package com.syncd.application.port.out.autentication;


import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;

public interface AuthenticationPort {
    TokenDto GetJwtTokens(UserForTokenDto user);
}

