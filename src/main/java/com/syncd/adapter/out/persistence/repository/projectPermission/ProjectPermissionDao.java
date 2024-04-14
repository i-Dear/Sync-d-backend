
package com.syncd.adapter.out.persistence.repository.projectPermission;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectPermissionDao extends MongoRepository<ProjectPermissionEntity, String> {
    Optional<ProjectPermissionEntity> findByKey_ProjectIdAndKey_UserId(String projectId, String userId);
    List<ProjectPermissionEntity> findAllByKey_UserId(String userId);

}
