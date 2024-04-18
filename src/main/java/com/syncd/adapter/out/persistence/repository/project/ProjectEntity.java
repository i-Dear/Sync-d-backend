package com.syncd.adapter.out.persistence.repository.project;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "projects")
public class ProjectEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private String img;
    private List<UserInProjectEntity> users;

    @Data
    public static class UserInProjectEntity {
        private String userId;
        private Role role;
    }
}