package com.syncd.adapter.out.persistence.repository.user;

import com.syncd.enums.UserAccountStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String tid;
    private String email;
    private String name;
    private UserAccountStatus status;

    @Field("profile_img")
    private String profileImg;

    @Field("project_ids")
    private List<String> projectIds = new ArrayList<>();
}