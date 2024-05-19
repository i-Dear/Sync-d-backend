package com.syncd.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.syncd.application.port.in.GenerateTokenUsecase;
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
public class LoginService implements SocialLoginUsecase {
    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId ;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String resourceUri;
    private final WriteUserPort writeUserPort;

    private final GenerateTokenUsecase generateTokenUsecase;

    private final ReadUserPort readUserPort;
    @Override
    public TokenDto socialLogin(String code, String registrationId) {
        String googleAccessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(googleAccessToken, registrationId);
        System.out.println("userResourceNode = " + userResourceNode);

        String userEmail = userResourceNode.get("email").asText();
        String userName = userResourceNode.get("name").asText();
        String userProfileImg = userResourceNode.get("picture").asText();
        System.out.println("id = " + userName);
        System.out.println("email = " + userEmail);
        System.out.println("img = " + userProfileImg);

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

    private String getAccessToken(String authorizationCode, String registrationId) {

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
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    private JsonNode getUserResource(String accessToken, String registrationId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}