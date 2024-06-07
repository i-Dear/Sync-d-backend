package com.syncd.adapter.in.web;

import com.syncd.GoogleOAuth2Properties;
import com.syncd.application.port.in.GetOauthRedirectUrlUsecase;
import com.syncd.application.port.in.SocialLoginUsecase;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class LoginController {
    private final GetOauthRedirectUrlUsecase getOauthRedirectUrlUsecase;

    @GetMapping("/login/google")
    public RedirectView redirectToGoogleOAuth(HttpServletRequest request) {
        String referer = getReferer(request);
        String url = getOauthRedirectUrlUsecase.getOauthRedirectUrlUsecase(referer);
        System.out.println("URL: " + url);
        return new RedirectView(url);
    }

    private String getReferer(HttpServletRequest request){

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
            }
        } else if (referer!=null) {
            refererSubstring=referer;
        } else {
            refererSubstring = "https://syncd.i-dear.org/";
        }
        return refererSubstring;
    }
}