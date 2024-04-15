package com.syncd.adapter.out.persistence.repository.project;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectDao extends MongoRepository<ProjectEntity, String> {
}
