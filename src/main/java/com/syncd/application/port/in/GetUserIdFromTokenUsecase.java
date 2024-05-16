package com.syncd.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface GetUserIdFromTokenUsecase {
    String getUserIdFromToken(String token);
}
