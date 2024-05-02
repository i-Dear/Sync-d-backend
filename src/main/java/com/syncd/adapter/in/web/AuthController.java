package com.syncd.adapter.in.web;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthController {
    private final ReadUserPort readUserPort;
    private final LoginService loginService;

    @GetMapping("/code/{registrationId}")
    public RedirectView googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpServletResponse response) {
        TokenDto token = loginService.socialLogin(code, registrationId);
        response.setHeader("Authorization", "Bearer " + token.accessToken());
        return new RedirectView("http://localhost:3000");
    }
}