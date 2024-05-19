package com.syncd.application.port.in;

import com.syncd.dto.TokenDto;

public interface SocialLoginUsecase {
    TokenDto socialLogin(String code, String registrationId, String redirectionUri);
}
