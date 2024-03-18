package com.syncd.domain.dao;

import com.syncd.domain.entity.OrganizationPermissionEntity;
import com.syncd.domain.entity.WorkspaceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceDao extends MongoRepository<WorkspaceEntity, String> {
}
