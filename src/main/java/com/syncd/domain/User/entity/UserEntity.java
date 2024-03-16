package com.syncd.domain.User.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class UserEntity {
    @Id
    private String email;
    private String password;
    private Integer age;
}
