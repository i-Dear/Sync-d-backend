package com.syncd.adapter.out.persistence.repository.question;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionDao extends MongoRepository<QuestionEntity, String> {

}
