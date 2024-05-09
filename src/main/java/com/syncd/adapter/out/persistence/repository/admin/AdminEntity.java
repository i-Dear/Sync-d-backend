package com.syncd.adapter.out.persistence.repository.admin;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "admins")
public class AdminEntity {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
}