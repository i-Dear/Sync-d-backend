package com.syncd.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.syncd.GoogleOAuth2Properties;
import com.syncd.application.port.in.GenerateTokenUsecase;
import com.syncd.application.port.in.GetOauthRedirectUrlUsecase;
import com.syncd.application.port.in.SocialLoginUsecase;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import com.syncd.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
@RequiredArgsConstructor
public class LoginService implements SocialLoginUsecase, GetOauthRedirectUrlUsecase {
    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId ;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String resourceUri;
    private final WriteUserPort writeUserPort;

    private final GoogleOAuth2Properties googleOAuth2Properties;

    private final GenerateTokenUsecase generateTokenUsecase;

    private final ReadUserPort readUserPort;
    @Override
    public TokenDto socialLogin(String code, String registrationId, String redirectionUri) {
        String googleAccessToken = getAccessToken(code, registrationId,redirectionUri);
        System.out.println("accesstoken"+googleAccessToken);
        JsonNode userResourceNode = getUserResource(googleAccessToken, registrationId);

        String userEmail = userResourceNode.get("email").asText();
        String userName = userResourceNode.get("name").asText();
        String userProfileImg = userResourceNode.get("picture").asText();

        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        user.setProfileImg(userProfileImg);
        if(readUserPort.isExistUser(userEmail)==false){
            user.setId(writeUserPort.createUser(user.getName(),user.getEmail(),user.getProfileImg()).value());
        }else{
            user.setId(readUserPort.findByEmail(userEmail).getId());
        }

        String accessToken = generateTokenUsecase.generateToken(user);
        System.out.println("JWT accessToken : " + accessToken);
        return new TokenDto(accessToken,"");
    }

    private String getAccessToken(String authorizationCode, String registrationId, String redirectUri) {
        System.out.println(redirectUri);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);
        System.out.print(tokenUri);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
//        if (responseNode.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("Failed to get access token");
//        }
        JsonNode accessTokenNode = responseNode.getBody();
//        if (accessTokenNode == null || !accessTokenNode.has("access_token")) {
//            throw new RuntimeException("Access token is missing in the response");
//        }
        return accessTokenNode.get("access_token").asText();
    }
    private JsonNode getUserResource(String accessToken, String registrationId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    @Override
    public String getOauthRedirectUrlUsecase(String referer) {

        String redirectUrl = googleOAuth2Properties.getRedirectUri();

        String url = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=448582571570-km2g33b06432q3ahl8pathc0tln7g0i4.apps.googleusercontent.com" +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

        if(referer.contains("localhost")){
            url = "https://accounts.google.com/o/oauth2/auth" +
                    "?client_id=448582571570-km2g33b06432q3ahl8pathc0tln7g0i4.apps.googleusercontent.com" +
                    "&redirect_uri=" + redirectUrl + "/dev"+
                    "&response_type=code" +
                    "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
        }
        return url;
    }
}