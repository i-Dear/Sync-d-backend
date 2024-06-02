package com.syncd.application.service;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetUserInfoUsecase;
//import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.in.UpdateUserInfoUsecase;
import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.domain.user.User;
//import com.syncd.dto.TokenDto;
//import com.syncd.dto.UserForTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
@Primary
@RequiredArgsConstructor
public class UserService implements GetUserInfoUsecase, UpdateUserInfoUsecase {
    private final ReadUserPort readUserPort;
    private final WriteUserPort writeUserPort;
    private final AuthenticationPort authenticationPort;
    private final S3Port s3Port;
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;

//    @Override
//    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerDto){
//        String userId = writeUserPort.createUser(registerDto.name(), registerDto.email(),"").value();
//        TokenDto tokens = authenticationPort.GetJwtTokens(new UserForTokenDto(userId));
//        return new RegisterUserResponseDto(tokens.accessToken(), tokens.refreshToken());
//    }

    @Override
    public GetUserInfoResponseDto getUserInfo(String userId) {
        User user = readUserPort.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto projects = getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId);

        return new GetUserInfoResponseDto(userId, user.getName(), user.getProfileImg(), user.getEmail(), projects.projects());

    }

    @Override
    public UpdateUserInfoResponseDto updateUserInfo(String userId, String name, MultipartFile img) {
        User user = readUserPort.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        String imgURL = "";
        if (img != null && !img.isEmpty()) {
            Optional<String> optionalImgURL = s3Port.uploadMultipartFileToS3(img);
            imgURL = optionalImgURL.orElseThrow(() -> new IllegalStateException("Failed to upload image to S3"));
            user.setProfileImg(imgURL);
        }

        writeUserPort.updateUser(user);

        return new UpdateUserInfoResponseDto(userId, user.getName(), user.getProfileImg());
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
