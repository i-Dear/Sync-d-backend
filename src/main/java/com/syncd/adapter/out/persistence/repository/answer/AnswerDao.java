package com.syncd.adapter.out.persistence.repository.answer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerDao extends MongoRepository<AnswerEntity, String> {

}
