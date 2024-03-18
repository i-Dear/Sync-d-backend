package com.syncd.domain.dao;

import com.syncd.domain.entity.OrganizationEntity;
import com.syncd.domain.entity.OrganizationPermissionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationPermissionDao extends MongoRepository<OrganizationPermissionEntity, String> {
}
