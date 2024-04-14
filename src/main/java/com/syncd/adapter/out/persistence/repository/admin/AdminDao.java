package com.syncd.adapter.out.persistence.repository.admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminDao extends MongoRepository<AdminEntity, String> {
    Optional<AdminEntity> findByEmail(String email);

}
