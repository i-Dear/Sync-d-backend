package com.syncd.domain.dao;

import com.syncd.domain.entity.OrganizationEntity;
import com.syncd.domain.entity.OrganizationPermissionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrganizationPermissionDao extends MongoRepository<OrganizationPermissionEntity, String> {
    List<OrganizationPermissionEntity> findByKey_UserId(String userId);
}
