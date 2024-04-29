package com.syncd.application.port.out.persistence.user;


import com.syncd.dto.UserDto;

public interface ReadUserPort {
    UserDto findByEmail(String email);

    UserDto findByUsername(String username);
    boolean isExistUser(String email);
}
