package com.syncd.adapter.out.persistence.repository.user;

import com.syncd.enums.UserAccountStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    @Field("user_id")
    private String userId;
    private String email;
    private String password;
    private String name;
    private UserAccountStatus status;

    @Field("profile_img")
    private String profileImg;

    @Builder.Default
    @Field("project_ids")
    private List<String> projectIds = new ArrayList<>();
}