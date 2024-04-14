package com.syncd.adapter.out.persistence.repository.projectPermission;

import com.syncd.enums.Role;
import com.syncd.enums.RoomPermission;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "project_permissions")
public class ProjectPermissionEntity {
    @Id
    private ProjectPermissionKey key;

    private Role role;
    @Field("room_permission")
    private RoomPermission roomPermission;

    @Data
    @Builder
    @EqualsAndHashCode
    static public class ProjectPermissionKey {
        private String userId;
        private String projectId;

    }

//    @Builder
//    @Data
//    public static class Authentication {
//        private RoomPermission room1;
//        private RoomPermission room2;
//        private RoomPermission room3_1;
//        private RoomPermission room3_2;
//        private RoomPermission room3_3;
//        private RoomPermission room4;
//        private RoomPermission room5;
//        private RoomPermission room6;
//        private RoomPermission room7;
//    }

}



