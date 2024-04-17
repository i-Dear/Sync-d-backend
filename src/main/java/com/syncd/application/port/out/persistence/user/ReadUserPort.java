package com.syncd.application.port.out.persistence.user;


import com.syncd.dto.UserDto;

public interface ReadUserPort {
    UserDto findByEmail(String email);

    UserDto findByEmailAndPassword(String email, String password);
}
