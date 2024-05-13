package com.syncd.domain.project;

import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class UserInProject {
    private  String userId;
    private  Role role;

    public UserInProject(String userId, Role role){
        this.userId=userId;
        this.role = role;
    }

    public static List<UserInProject> userInProjectsFromUsers(String hostId, List<User> members){
        return Stream.concat(
                Stream.of(new UserInProject(hostId, Role.HOST)), // 호스트 사용자
                members.stream().map(el -> new UserInProject(el.getId(), Role.MEMBER))
        ).collect(Collectors.toList());
    }
}
