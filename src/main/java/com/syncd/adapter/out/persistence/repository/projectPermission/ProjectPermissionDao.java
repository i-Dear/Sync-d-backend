
package com.syncd.adapter.out.persistence.repository.projectPermission;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectPermissionDao extends MongoRepository<ProjectPermissionEntity, String> {
    Optional<ProjectPermissionEntity> findByProjectIdAndUserId(String projectId, String userId);

    List<ProjectPermissionEntity> findAllByUserId(String userId);

}
