package com.syncd.domain.User;

import com.syncd.domain.User.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDao extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
