package com.syncd.domain.entity;

import com.syncd.domain.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document(collection = "organization_permissions")
public class OrganizationPermissionEntity {
    @Id
    private String id; // MongoDB의 문서 ID

    private OrganizationPermissionKey key; // 복합 키를 포함하는 필드
    private Role role;

    @Data
    public static class OrganizationPermissionKey {
        private String userId;
        private String organizationId;

    }

}
