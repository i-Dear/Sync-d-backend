package com.syncd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-c")
public class AuthControllerProperties {
    private String redirectUrl;

    private String redirectUrlDev;

    private String redirectUriForGoogle;

    private String redirectUriForGoogleDev;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getRedirectUrlDev() {
        return redirectUrlDev;
    }

    public String getRedirectUriForGoogle() {return redirectUriForGoogle;}

    public String getRedirectUriForGoogleDev() {return redirectUriForGoogleDev;}

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public void setRedirectUrlDev(String redirectUrlDev) {
        this.redirectUrlDev = redirectUrlDev;
    }

    public void setRedirectUriForGoogle(String redirectUriForGoogle) {this.redirectUriForGoogle = redirectUriForGoogle;}

    public void setRedirectUriForGoogleDev(String redirectUriForGoogleDev) {this.redirectUriForGoogleDev = redirectUriForGoogleDev;}
}
