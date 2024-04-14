package com.syncd.application.port.out.persistence.user;

import com.syncd.application.port.out.persistence.user.dto.UserId;

public interface WriteUserPort {
    UserId createUser(String userName, String email, String password);
}
