package com.syncd.adapter.in.web;

import com.syncd.AuthControllerProperties;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import com.syncd.exceptions.ProjectAlreadyExistsException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthController {
    private final LoginService loginService;
    private final AuthControllerProperties authControllerProperties;

    @GetMapping("/code/{registrationId}")
    public RedirectView googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpServletResponse response) {
        String url = authControllerProperties.getRedirectUrl();
        TokenDto token = loginService.socialLogin(code, registrationId);
        String redirectUrl = url + token.accessToken();
        return new RedirectView(redirectUrl);
    }
    // @GetMapping("/test/sentry")
    // public ResponseEntity<String> testSentryIntegration() {
    //     throw new ProjectAlreadyExistsException("Test Exception to verify Sentry logging.");
    // }
}
