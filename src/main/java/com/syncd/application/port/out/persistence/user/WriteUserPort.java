package com.syncd.application.port.out.persistence.user;


import com.syncd.dto.UserId;

public interface WriteUserPort {
    UserId createUser(String userName, String email,String img);
}
