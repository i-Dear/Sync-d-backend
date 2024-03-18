package com.syncd.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document(collection = "workspaces")
public class WorkspaceEntity {
    @Id
    private String workspaceId;
    private String organizationId;
    private String name;
    private String description;
}
