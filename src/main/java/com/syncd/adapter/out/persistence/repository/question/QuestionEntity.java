package com.syncd.adapter.out.persistence.repository.question;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "questions")
public class QuestionEntity {
    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("question_text")
    private String questionText;

    @Field("admin_id")
    private String adminId;

    @Field("answer_text")
    private String answerText;
}