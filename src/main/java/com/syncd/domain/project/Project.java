package com.syncd.domain.project;

import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Project createProjectDomain(String projectName, String description, String img, String hostId, List<User> users){
        Project project = new Project();
        project.setImg(img);
        project.setUsers( userInProjectsFromUsers(hostId,users));
        project.setName(projectName);
        project.setDescription(description);
        project.setProgress(0);
        project.setLastModifiedDate(LocalDateTime.now().toString());
        return project;
    }
     private List<UserInProject> userInProjectsFromUsers(String hostId, List<User> members){
         if (members == null) {
             return Collections.emptyList(); // 호스트는 존재하지만 멤버는 없을 수 있음
         }
        return Stream.concat(
                Stream.of(new UserInProject(hostId, Role.HOST)), // 호스트 사용자
                members.stream().map(el -> new UserInProject(el.getId(), Role.MEMBER))
        ).collect(Collectors.toList());
    }
}
