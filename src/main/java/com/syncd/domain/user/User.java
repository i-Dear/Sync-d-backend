package com.syncd.domain.user;

import lombok.Data;
@Data
public class User {
    private String id;
    private String tid;
    private String name;
    private String email;
    private int numberOfLeftHostProjects;
    private String profileImg;
}
