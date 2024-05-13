package com.syncd.adapter.in.web;

import com.syncd.GoogleOAuth2Properties;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class LoginController {
    private final ReadUserPort readUserPort;
    private final LoginService loginService;
    private final GoogleOAuth2Properties googleOAuth2Properties;

    @GetMapping("/login/google")
    public RedirectView redirectToGoogleOAuth() {
        String redirectUrl = googleOAuth2Properties.getRedirectUri();
        String url = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=70988875044-9nmbvd2suleub4ja095mrh83qbi7140j.apps.googleusercontent.com" +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
        return new RedirectView(url);
    }
}