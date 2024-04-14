package com.syncd.adapter.out.persistence.repository.project;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectDao extends MongoRepository<ProjectEntity, String> {
}
