package com.syncd.domain.project;


import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Project {
    private final String id;
    private  String name;
    private  String description;
    private  String img;
    private  List<UserInProject> users;

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

    public List<String> getUserEmails(List<User> users) {
        if (users == null) {
            return null; // 사용자 목록이 없으면 null 반환
        }

        // UserInProject의 userId와 UserEntity의 id가 같은 사용자들의 이메일 주소만 추출하여 리스트로 반환
        return users.stream()
                .map(User::getEmail)
                .filter(email -> email != null && !email.isEmpty()) // 비어있는 이메일 주소 필터링
                .collect(Collectors.toList());
    }
}
