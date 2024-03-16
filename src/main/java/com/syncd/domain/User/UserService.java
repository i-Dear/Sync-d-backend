package com.syncd.domain.User;

import com.syncd.domain.User.dto.LoginDto;
import com.syncd.domain.User.dto.RegisterDto;
import com.syncd.domain.User.entity.UserEntity;
import com.syncd.domain.User.exceptions.LoginException;
import com.syncd.domain.User.exceptions.RegisterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public String resgisterUser(RegisterDto registerDto){
        if (registerDto.getEmail() == null || registerDto.getEmail().isEmpty() ||
                registerDto.getPassword() == null || registerDto.getPassword().isEmpty() ||
                registerDto.getAge() == null) {
            throw new RegisterException("Email, password, and age must be provided");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .age(registerDto.getAge())
                .build();
        userDao.save(userEntity);
        return registerDto.getEmail();
    }

    public Boolean loginUser(LoginDto loginDto) {
        return userDao.findByEmail(loginDto.getEmail())
                .map(userEntity -> {
                    if (loginDto.getPassword().equals(userEntity.getPassword())) {
                        return true;
                    } else {
                        throw new LoginException("Invalid password for email: " + loginDto.getEmail());
                    }
                })
                .orElseThrow(() -> new LoginException("User not found with email: " + loginDto.getEmail()));
    }
}
