package com.syncd.adapter.in.web;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final ReadUserPort readUserPort;

    @GetMapping("/login/google")
    public RedirectView oauthLogin(Authentication authentication, HttpServletRequest request) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2User: " + oAuth2User.getAttributes());

        String targetUrl = request.getHeader("Referer");
        if (targetUrl == null || targetUrl.isBlank()) {
            // 기본 URL 설정
            targetUrl = "/";
        }

        return new RedirectView(targetUrl);
    }
}