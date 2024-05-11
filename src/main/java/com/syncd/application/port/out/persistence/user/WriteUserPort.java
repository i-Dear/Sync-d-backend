package com.syncd.application.port.out.persistence.user;


import com.syncd.domain.user.User;
import com.syncd.dto.UserId;

public interface WriteUserPort {
    UserId createUser(String userName, String email,String img);

    UserId updateUser(User user);
}
