package com.syncd.adapter.out.persistence.repository.userInProject;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Builder
@Data
@Document(collection = "user_in_project")
public class UserInProjectEntity {
    @Id
    @Field("project_id")
    private String projectId;

    @Field("users")
    private List<String> users;

}
