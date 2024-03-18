package com.syncd.module.User;

import com.syncd.domain.dao.UserDao;
import com.syncd.domain.entity.UserEntity;
import com.syncd.module.User.dto.LoginDto;
import com.syncd.module.User.dto.RegisterDto;
import com.syncd.module.User.exceptions.LoginException;
import com.syncd.module.User.exceptions.RegisterException;
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
                .name(registerDto.getName())
                .build();
        System.out.print(userEntity);

        return userDao.save(userEntity).getUserId();
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
