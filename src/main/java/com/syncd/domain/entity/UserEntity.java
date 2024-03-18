package com.syncd.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String userId;
    private String email;
    private String password;
    private String name;
    private Integer age;

    @Builder.Default
    private List<String> organizationIds = new ArrayList<>();
}