package com.syncd.adapter.in.web;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final ReadUserPort readUserPort;

    @GetMapping("/login/google")
    public @ResponseBody String oauthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2User:" + oauth.getAttributes());
        return readUserPort.findByEmail(oAuth2User.getAttributes().get("email").toString()).getId();
    }
}
