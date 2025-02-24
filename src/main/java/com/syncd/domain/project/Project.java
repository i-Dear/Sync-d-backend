package com.syncd.domain.project;

import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import lombok.Data;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

@Data
public class Project {
    private  String id;
    private  String name;
    private  String description;
    private  String img;
    private  List<UserInProject> users;
    private int progress;
    private String lastModifiedDate;
    private int leftChanceForUserstory;

    // 싱크된 내용
    private String problem;
    private List<PersonaInfo> personaInfos;
    private String whyWhatHowImage;
    private CoreDetails coreDetails;
    private String businessModelImage;
    private List<String> scenarios;
    private List<Epic> epics;
    private String menuTreeImage;

    public void addUsers(List<UserInProject> newUsers) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.addAll(newUsers);
    }

    public void withdrawUsers(List<String> userIds) {
        if (this.users != null && !userIds.isEmpty()) {
            this.users = this.users.stream()
                    .filter(user -> !userIds.contains(user.getUserId()))
                    .collect(Collectors.toList());
        }
    }

    public String getHost() {
        return this.users.stream()
                .filter(user -> user.getRole() == Role.HOST)
                .map(UserInProject::getUserId)
                .findFirst()
                .orElse(null);  // Returns null if no host is found
    }

    public void updateProjectInfo(String projectName, String description, String img){
        this.img = img;
        this.name = projectName;
        this.description = description;
    }

    public void syncProject(int progress){
        this.progress = progress;
        this.lastModifiedDate = LocalDateTime.now().toString();
    }

    public void subLeftChanceForUserstory(){
        this.leftChanceForUserstory = this.leftChanceForUserstory-1;
    }

//    public Project(String projectName, String description, String img, List<UserInProject> users){
//        this.img = img;
//        this.users = users;
//        this.name = projectName;
//        this.description = description;
//        this.progress = 0;
//        this.lastModifiedDate = LocalDateTime.now().toString();
//    }
    public Project createProjectDomain(String projectName, String description, String img, String hostId){
        Project project = new Project();
        project.setImg(img);
        project.setUsers( userInProjectsFromUsers(hostId));
        project.setName(projectName);
        project.setDescription(description);
        project.setProgress(0);

        // 한국 시간대로 설정
        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        project.setLastModifiedDate(nowInKorea.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        project.setLeftChanceForUserstory(3);
        return project;
    }
     private List<UserInProject> userInProjectsFromUsers(String hostId){
        List<User> members = new ArrayList<>();
        return Stream.concat(
                Stream.of(new UserInProject(hostId, Role.HOST)), // 호스트 사용자
                members.stream().map(el -> new UserInProject(el.getId(), Role.MEMBER))
        ).collect(Collectors.toList());
    }

    public String getImgFileName() {
        try {
            URL url = new URL("https://" + img);
            String path = url.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // URL 형식이 잘못되었거나 기타 오류 처리
        }
    }
}
