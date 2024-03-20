
package com.syncd.domain.dao;

import com.syncd.domain.entity.WorkspaceEntity;
import com.syncd.domain.entity.WorkspacePermissionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface WorkspacePermissionDao extends MongoRepository<WorkspacePermissionEntity, String> {
    Optional<WorkspacePermissionEntity> findByKey_UserIdAndKey_WorkspaceId(String userId, String workspaceId);
}
