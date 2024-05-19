package com.syncd.adapter.in.web;

import com.syncd.AuthControllerProperties;
import com.syncd.application.port.in.SocialLoginUsecase;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthController {
    private final SocialLoginUsecase socialLoginUsecase;
    private final AuthControllerProperties authControllerProperties;

    @GetMapping("/code/{registrationId}")
    public RedirectView googleLogin(@RequestParam String code,
                                    @PathVariable String registrationId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        String url = authControllerProperties.getRedirectUrl();
        TokenDto token = socialLoginUsecase.socialLogin(code, registrationId);

        Enumeration<String> reqHeaderNames = request.getHeaderNames();

        while (reqHeaderNames.hasMoreElements()) {
            String headerName = reqHeaderNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
        String cookies = request.getHeader("cookie");
        String referer = request.getHeader("referer");
        String refererSubstring="";
        if(cookies!=null){
            int refererIndex = cookies.indexOf("referer=");
            if (refererIndex != -1) {
                // 'referer=' 이후의 부분 추출
                refererSubstring = cookies.substring(refererIndex + "referer=".length());

                // ';' 이전의 부분 추출 (쿠키가 끝날 때까지)
                int semicolonIndex = refererSubstring.indexOf(';');
                if (semicolonIndex != -1) {
                    refererSubstring = refererSubstring.substring(0, semicolonIndex);
                }

                System.out.println("Referer: " + refererSubstring);
            }
        } else if (referer!=null) {
            System.out.println("Referer from referer");
            refererSubstring=referer;
        } else {
            System.out.println("Referer not found.");
            refererSubstring = "https://syncd.i-dear.org/";
        }

        String redirectUrl = refererSubstring + url + token.accessToken();
        return new RedirectView(redirectUrl);
    }
}
