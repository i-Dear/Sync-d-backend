package com.syncd.domain.project;

import lombok.Data;

import java.util.List;

@Data
public class Epic {
    private String id;
    private String name;
    private List<UserStory> userStories;
}
