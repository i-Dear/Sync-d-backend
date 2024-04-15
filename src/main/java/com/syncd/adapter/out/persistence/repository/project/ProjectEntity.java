package com.syncd.adapter.out.persistence.repository.project;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "projects")
public class ProjectEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private String img;
}
