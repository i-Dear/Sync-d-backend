package com.syncd.adapter.in.web;

import com.syncd.application.port.in.GetOauthRedirectUrlUsecase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        System.out.println("Extracted Referer: " + referer);
        String url = getOauthRedirectUrlUsecase.getOauthRedirectUrlUsecase(referer);
        System.out.println("Redirect URL: " + url);
        return new RedirectView(url);
    }

    private String getReferer(HttpServletRequest request) {
        // Print all headers for debugging
        Enumeration<String> reqHeaderNames = request.getHeaderNames();
        while (reqHeaderNames.hasMoreElements()) {
            String headerName = reqHeaderNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }

        // Attempt to get referer from headers
        String referer = request.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            System.out.println("Referer from Header: " + referer);
            return referer;
        }

        // Attempt to get referer from cookies
        String cookies = request.getHeader("cookie");
        if (cookies != null) {
            int refererIndex = cookies.indexOf("referer=");
            if (refererIndex != -1) {
                String refererSubstring = cookies.substring(refererIndex + "referer=".length());
                int semicolonIndex = refererSubstring.indexOf(';');
                if (semicolonIndex != -1) {
                    refererSubstring = refererSubstring.substring(0, semicolonIndex);
                }
                System.out.println("Referer from Cookie: " + refererSubstring);
                return refererSubstring;
            }
        }

        // Default referer
        String defaultReferer = "https://syncd.i-dear.org/";
        System.out.println("Default Referer: " + defaultReferer);
        return defaultReferer;
    }
}
