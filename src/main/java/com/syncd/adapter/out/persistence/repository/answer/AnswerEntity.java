package com.syncd.adapter.out.persistence.repository.answer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "answers")
public class AnswerEntity {
    @Id
    @Field("answer_id")
    private String answerId;

    @Field("question_id")
    private String questionId;

    @Field("admin_id")
    private String adminId;

    private String text;
}