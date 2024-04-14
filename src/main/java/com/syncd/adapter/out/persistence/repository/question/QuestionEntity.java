package com.syncd.adapter.out.persistence.repository.question;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "questions")
public class QuestionEntity {
    @Id
    @Field("question_id")
    private String questionId;

    @Field("user_id")
    private String userId;

    @Field("question_text")
    private String questionText;

    @Field("admin_id")
    private String adminId;

    @Field("answer_text")
    private String answerText;
}