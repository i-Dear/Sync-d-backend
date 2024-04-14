package com.syncd.adapter.out.persistence.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDao extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

}
