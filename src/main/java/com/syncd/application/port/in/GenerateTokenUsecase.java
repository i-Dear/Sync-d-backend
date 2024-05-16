package com.syncd.application.port.in;

import com.syncd.domain.user.User;

public interface GenerateTokenUsecase {
    String generateToken(User user);
}
