package com.syncd.application.port.out.autentication;

import com.syncd.application.port.out.autentication.dto.TokenDto;
import com.syncd.application.port.out.autentication.dto.UserForTokenDto;

public interface AuthenticationPort {
    TokenDto GetJwtTokens(UserForTokenDto user);
}

