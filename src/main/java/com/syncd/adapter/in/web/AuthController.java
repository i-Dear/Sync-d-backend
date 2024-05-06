package com.syncd.adapter.in.web;

import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthController {
    private final LoginService loginService;

    @GetMapping("/code/{registrationId}")
    public RedirectView googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpServletResponse response) {
        TokenDto token = loginService.socialLogin(code, registrationId);
        String redirectUrl = "https://syncd.i-dear.org/org/dashboard?token=" + token.accessToken();
        return new RedirectView(redirectUrl);
    }
}
