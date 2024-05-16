package com.syncd.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface ResolveTokenUsecase {
    String resolveToken(HttpServletRequest request);
}
