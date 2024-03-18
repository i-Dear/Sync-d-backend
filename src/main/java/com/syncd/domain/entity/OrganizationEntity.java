package com.syncd.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document(collection = "organizations")
public class OrganizationEntity {
    @Id
    private String organizationId;
    private String name;
    private String description;
    private List<String> workspaceIds;
}
