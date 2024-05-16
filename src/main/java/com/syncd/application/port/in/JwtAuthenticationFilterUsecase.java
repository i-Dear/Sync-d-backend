package com.syncd.application.port.in;

import com.syncd.application.service.JwtService;
import com.syncd.domain.user.User;

public interface JwtAuthenticationFilterUsecase {
     jakarta.servlet.Filter JwtAuthenticationFilter(ResolveTokenUsecase resolveTokenUsecase, ValidateTokenUsecase validateTokenUsecase);
}
