package com.syncd.application.port.out.persistence.user;

import com.syncd.application.port.out.persistence.user.dto.UserDto;

public interface ReadUserPort {
    UserDto findByEmail(String email);

    UserDto findByEmailAndPassword(String email, String password);
}
