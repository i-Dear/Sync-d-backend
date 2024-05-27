package domain.user;

import com.syncd.domain.user.User;
import com.syncd.enums.Role;
import com.syncd.domain.project.UserInProject;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInProjectTest {

    @Test
    public void testUserInProjectsFromUsers() {
        // 호스트 사용자 ID
        String hostId = "host123";

        // 멤버 사용자 리스트 생성
        User user1 = new User();
        user1.setId("1");
        user1.setName("donggni");
        user1.setEmail("donggni@example.com");
        user1.setProfileImg("profile1.jpg");

        User user2 = new User();
        user2.setId("2");
        user2.setName("jalju");
        user2.setEmail("jalju.smith@example.com");
        user2.setProfileImg("profile2.jpg");

        List<User> members = Arrays.asList(user1, user2);

        // UserInProject 리스트 생성
        List<UserInProject> userInProjects = UserInProject.userInProjectsFromUsers(hostId, members);

        // 예상 결과
        assertEquals(3, userInProjects.size());

        // 호스트 검증
        assertEquals("host123", userInProjects.get(0).getUserId());
        assertEquals(Role.HOST, userInProjects.get(0).getRole());

        // 멤버 검증
        assertEquals("1", userInProjects.get(1).getUserId());
        assertEquals(Role.MEMBER, userInProjects.get(1).getRole());

        assertEquals("2", userInProjects.get(2).getUserId());
        assertEquals(Role.MEMBER, userInProjects.get(2).getRole());
    }
}

