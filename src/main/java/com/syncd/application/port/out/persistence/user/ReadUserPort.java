package com.syncd.application.port.out.persistence.user;


import com.syncd.domain.user.User;

public interface ReadUserPort {
    User findByEmail(String email);

    User findByUsername(String username);

    User findByUserId(String username);
    boolean isExistUser(String email);
}
