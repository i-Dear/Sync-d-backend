package com.syncd.application.domain.project;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class Project {
    private final String projectId;
    private final String name;
    private final String description;
    private  List<String> projectIds;
    private  List<UserInProject> users;

    // 여러 사용자 추가
    public void addUsers(List<UserInProject> newUsers) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.addAll(newUsers);
    }

    // 여러 사용자 제거
    public void withdrawUsers(List<String> userIds) {
        if (this.users != null && !userIds.isEmpty()) {
            this.users = this.users.stream()
                    .filter(user -> !userIds.contains(user.getUserId()))
                    .collect(Collectors.toList());
        }
    }

    // 팀 정보 업데이트
    public Project updateTeamInfo(String newName, String newDescription, List<String> newProjectIds) {
        return new Project(this.projectId, newName, newDescription, newProjectIds, this.users);
    }

}
