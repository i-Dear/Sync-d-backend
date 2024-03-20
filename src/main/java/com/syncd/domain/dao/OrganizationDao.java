package com.syncd.domain.dao;

import com.syncd.domain.entity.OrganizationEntity;
import com.syncd.domain.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OrganizationDao extends MongoRepository<OrganizationEntity, String> {
}
