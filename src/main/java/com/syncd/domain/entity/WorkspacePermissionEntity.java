package com.syncd.domain.entity;

import com.syncd.domain.enums.Role;
import com.syncd.domain.enums.RoomPermission;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document(collection = "workspace_permissions")
public class WorkspacePermissionEntity {
    @Id
    private String id;

    private WorkspacePermissionKey key;

    private Role role;
    private Authentication authentication;

    @Data
    public class WorkspacePermissionKey {
        private String userId;
        private String workspaceId;

    }
    @Builder
    @Data
    public static class Authentication {
        private RoomPermission room1;
        private RoomPermission room2;
        private RoomPermission room3_1;
        private RoomPermission room3_2;
        private RoomPermission room3_3;
        private RoomPermission room4;
        private RoomPermission room5;
        private RoomPermission room6;
        private RoomPermission room7;
    }

}



