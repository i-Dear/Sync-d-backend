package com.syncd.application.service;

import com.syncd.application.port.in.GetUserInfoUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class UserService implements RegitsterUserUsecase, GetUserInfoUsecase {
    private final ReadUserPort readUserPort;
    private final WriteUserPort writeUserPort;

    private final AuthenticationPort authenticationPort;

    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerDto){
        String userId = writeUserPort.createUser(registerDto.name(), registerDto.email(),"").value();
        TokenDto tokens = authenticationPort.GetJwtTokens(new UserForTokenDto(userId));
        return new RegisterUserResponseDto(tokens.accessToken(), tokens.refreshToken());
    }

    @Override
    public GetUserInfoResponseDto getUserInfo(String userId) {
        User user = readUserPort.findByUserId(userId);

        return new GetUserInfoResponseDto(user.getName(), user.getProfileImg(), user.getEmail());

    }

//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 데이터베이스에서 사용자 정보를 조회하는 로직을 구현합니다.
//        User user = readUserPort.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        return new org.springframework.security.core.userdetails.User(user.getName(), user.g(), user.getAuthorities());
//    }

}
