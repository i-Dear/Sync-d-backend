package com.syncd.adapter.out.persistence.repository.userInProject;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserInProjectDao extends MongoRepository<UserInProjectEntity, String> {
    Optional<UserInProjectEntity> findByProjectId(String projectId);
    List<UserInProjectEntity> findByUsersContains(String userId);
}
