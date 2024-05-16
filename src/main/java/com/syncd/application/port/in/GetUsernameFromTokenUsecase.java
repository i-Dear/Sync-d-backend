package com.syncd.application.port.in;

import com.syncd.domain.user.User;

public interface GetUsernameFromTokenUsecase {
    String getUsernameFromToken(String token);
}
