package com.syncd.adapter.in.web;

import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dev/auth")
public class DevController {
    private final LoginService loginService;

    @GetMapping("/code/{registrationId}")
    public RedirectView googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpServletResponse response) {
        TokenDto token = loginService.socialLogin(code, registrationId);
        response.setHeader("Authorization", "Bearer " + token.accessToken());
        return new RedirectView("http://localhost:3000");
    }
}
