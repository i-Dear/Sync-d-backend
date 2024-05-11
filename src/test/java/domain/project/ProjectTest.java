package domain.project;

import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectTest {
    private Project project;
    private User user1;
    private User user2;
    private UserInProject userInProject1;
    private UserInProject userInProject2;


    @BeforeEach
    void setup() {
        userInProject1 = new UserInProject("user1", Role.MEMBER);
        userInProject2 = new UserInProject("user2", Role.MEMBER);

        user1 = new User();
        user1.setId("user1");
        user1.setName("1");
        user1.setEmail("이찬주@example.com");
        user1.setProfileImg("profile.jpg");

        user2 = new User();
        user2.setId("user2");
        user2.setName("2");
        user2.setEmail("이찬주@example.com");
        user2.setProfileImg("profile.jpg");

        String hostId = "hostUserId";
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        project = new Project("Project Name", "syncd", "img", hostId, userList);
        project.setId("1");
    }

    @Test
    @DisplayName("프로젝트에 사용자 추가 테스트")
    void testAddUser(){
        assertThat(project.getUsers()).hasSize(3).contains(userInProject1, userInProject2); // host, user1, user2
    }

    @Test
    @DisplayName("특정 사용자 제거 테스트")
    void testWithdrawUsers(){
        project.withdrawUsers(Arrays.asList("user1"));
        List<UserInProject> expectedUsers = Arrays.asList(
                new UserInProject("hostUserId", Role.HOST),
                new UserInProject("user2", Role.MEMBER) // user2의 역할을 MEMBER로 변경
        );
        assertThat(project.getUsers()).containsExactlyInAnyOrderElementsOf(expectedUsers);
    }

    @Test
    @DisplayName("호스트 유저 식별 테스트")
    void testGetHost(){
        assertThat(project.getHost()).isEqualTo("hostUserId");
    }

    @Test
    @DisplayName("호스트 없을 때 테스트")
    void testGetHostWhenNoHost(){
        project.setUsers(Arrays.asList(userInProject1));
        assertThat(project.getHost()).isNull();
    }
}
