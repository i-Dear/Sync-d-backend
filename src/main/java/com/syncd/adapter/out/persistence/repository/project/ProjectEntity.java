package com.syncd.adapter.out.persistence.repository.project;

import com.syncd.domain.project.PersonaInfo;
import com.syncd.enums.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "projects")
public class ProjectEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private String img;
    private List<UserInProjectEntity> users;
    private int progress;
    private String lastModifiedDate;
    private int leftChanceForUserstory;

    // 추가 필드
    private String problem;
    private List<PersonaInfo> personaInfos;
    private String whyWhatHowImage;
    private CoreDetails coreDetails;
    private String businessModelImage;
    private List<String> scenarios;
    private List<Epic> epics;
    private String menuTreeImage;

    @Data
    public static class UserInProjectEntity {
        private String userId;
        private Role role;
    }

    @Data
    public static class CoreDetails {
        private String coreTarget;
        private String coreProblem;
        private String coreCause;
        private String solution;
        private String coreValue;
    }

    @Data
    public static class Epic {
        private String id;
        private String name;
        private List<UserStory> userStories;
    }

    @Data
    public static class UserStory {
        private String id;
        private String name;
    }
}