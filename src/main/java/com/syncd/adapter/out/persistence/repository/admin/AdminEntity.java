package com.syncd.adapter.out.persistence.repository.admin;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Document(collection = "admins")
public class AdminEntity {
    @Id
    @Field("admin_id")
    private String adminId;
    private String email;
    private String password;
    private String name;
}