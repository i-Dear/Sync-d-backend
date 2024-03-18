package com.syncd.module.Room.dto;

import com.syncd.domain.entity.OrganizationEntity;
import com.syncd.domain.entity.OrganizationPermissionEntity;
import com.syncd.domain.entity.WorkspaceEntity;
import com.syncd.domain.entity.WorkspacePermissionEntity;
import com.syncd.domain.enums.Role;
import com.syncd.domain.enums.RoomPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllInfoAboutRoomsByUserIdResponseDto {
    private String userId;
    private List<Organization> organizations;

    public void createOrganizationFromEntity(
            OrganizationEntity organizationEntity,
            List<WorkspaceEntity> workspaceEntities,
            List<OrganizationPermissionEntity> organizationPermissionEntities,
            List<WorkspacePermissionEntity> workspacePermissionEntities) {

        // 조직의 권한 정보 찾기
        Role organizationRole = organizationPermissionEntities.stream()
                .filter(permission -> permission.getKey().getOrganizationId().equals(organizationEntity.getOrganizationId()))
                .findFirst()
                .map(OrganizationPermissionEntity::getRole)
                .orElse(null); // 권한 정보가 없다면 null 또는 기본 권한을 설정

        // 각 워크스페이스에 대한 DTO 생성
        List<Workspace> workspaces = workspaceEntities.stream()
                .filter(workspace -> workspace.getOrganizationId().equals(organizationEntity.getOrganizationId())) // 해당 조직에 속한 워크스페이스만 필터링
                .map(workspaceEntity -> {
                    // 워크스페이스에 대한 권한 정보 찾기
                    WorkspacePermissionEntity workspacePermission = workspacePermissionEntities.stream()
                            .filter(permission -> permission.getKey().getWorkspaceId().equals(workspaceEntity.getWorkspaceId()))
                            .findFirst()
                            .orElse(null);

                    return Workspace.fromEntity(workspaceEntity, workspacePermission); // Workspace DTO 생성
                })
                .collect(Collectors.toList());

        this.organizations.add(Organization.builder()
                .orgName(organizationEntity.getName())
                .orgId(organizationEntity.getOrganizationId())
                .orgDescription(organizationEntity.getDescription())
                .role(organizationRole)
                .workspaces(workspaces)
                .build());
    }

    @Builder
    @Data
    static class Organization{
        private String orgName;
        private String orgId;
        private String orgDescription;
        private Role role;
        private List<Workspace> workspaces;
    }

    @Builder
    @Data
    static class Workspace{
        private String workspaceName;
        private String workspaceId;
        private String workspaceDescription;
        private Role role;
        private RoomAuthentication roomAuthentication;

        private static Workspace fromEntity(WorkspaceEntity workspaceEntity, WorkspacePermissionEntity workspacePermission) {
            // RoomAuthentication 객체를 구성하는 예시 로직
            // 실제 구현에서는 workspacePermission 객체의 내용에 따라 RoomAuthentication을 생성해야 합니다.
            RoomAuthentication roomAuthentication = null;
            if (workspacePermission != null && workspacePermission.getAuthentication() != null) {
                // workspacePermission.getAuthentications()는 List<Authentication> 타입을 반환한다고 가정합니다.
                // 이 예시에서는 첫 번째 Authentication 객체를 사용합니다. 실제 구현에서는 필요에 맞게 수정해야 합니다.
                WorkspacePermissionEntity.Authentication authentication = workspacePermission.getAuthentication();
                roomAuthentication = RoomAuthentication.builder()
                        .room1(authentication.getRoom1())
                        .room2(authentication.getRoom2())
                        .room3_1(authentication.getRoom3_1())
                        .room3_2(authentication.getRoom3_2())
                        .room3_3(authentication.getRoom3_3())
                        .room4(authentication.getRoom4())
                        .room5(authentication.getRoom5())
                        .room6(authentication.getRoom6())
                        .room7(authentication.getRoom7())
                        .build();
            }

            return Workspace.builder()
                    .workspaceId(workspaceEntity.getWorkspaceId())
                    .workspaceName(workspaceEntity.getName())
                    .workspaceDescription(workspaceEntity.getDescription())
                    .role(workspacePermission != null ? workspacePermission.getRole() : null)
                    .roomAuthentication(roomAuthentication)
                    .build();
        }
    }

    @Builder
    @Data
    static class RoomAuthentication{
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

