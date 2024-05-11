package com.syncd.domain.project;

import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class UserInProject {
    private final String userId;
    private final Role role;

    public static List<UserInProject> userInProjectsFromUsers(String hostId, List<User> members){
        return Stream.concat(
                Stream.of(new UserInProject(hostId, Role.HOST)), // 호스트 사용자
                members.stream().map(el -> new UserInProject(el.getId(), Role.MEMBER))
        ).collect(Collectors.toList());
    }
}
