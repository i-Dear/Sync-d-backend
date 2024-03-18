package com.syncd.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@Data
public class WorkspaceEntity {
    @Id
    private String workspaceId;
    private String organizationId;
    private String name;
    private String description;
    private List<String> userIds;
}
