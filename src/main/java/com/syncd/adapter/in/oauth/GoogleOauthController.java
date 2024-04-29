package com.syncd.adapter.in.oauth;

import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthController extends DefaultOAuth2UserService {
    private final WriteUserPort writeUserPort;

    private final ReadUserPort readUserPort;
    // 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: "+userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String userName = oAuth2User.getAttributes().get("name").toString();
        String userEmail = oAuth2User.getAttributes().get("email").toString();
        String userProfileImg = oAuth2User.getAttributes().get("picture").toString();
        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        user.setProfileImg(userProfileImg);
        if(readUserPort.isExistUser(userEmail)==false){
            user.setId(writeUserPort.createUser(user.getName(),user.getEmail(),user.getProfileImg()).value());
        }else{
            user.setId(readUserPort.findByEmail(userEmail).getId());
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}