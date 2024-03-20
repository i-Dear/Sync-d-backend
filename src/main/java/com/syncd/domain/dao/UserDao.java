package com.syncd.domain.dao;

import com.syncd.domain.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserDao extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

}
