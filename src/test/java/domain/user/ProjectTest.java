package domain.user;

import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.enums.Role;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ProjectTest {
    private Project project;
    private UserInProject user1;
    private UserInProject user2;

    @BeforeEach
    void setup() {
        user1 = new UserInProject("user1", Role.MEMBER);
        user2 = new UserInProject("user2", Role.HOST);

        project = new Project("1");
        project.setName("syncd");
        project.setDescription("싱크해요");
        project.setImg("img.png");
        project.setUsers(Arrays.asList(user1, user2));
    }

    @Test
    @DisplayName("프로젝트에 사용자 추가 테스트")
    void testAddUser(){
        assertThat(project.getUsers()).hasSize(2).contains(user1, user2);
    }

    @Test
    @DisplayName("특정 사용자 제거 테스트")
    void testWithdrawUsers(){
        project.withdrawUsers(Arrays.asList("user1"));
        assertThat(project.getUsers()).hasSize(1).containsExactly(user2);
    }

    @Test
    @DisplayName("호스트 유저 식별 테스트")
    void testGetHost(){
        assertThat(project.getHost()).isEqualTo("user2");
    }

    @Test
    @DisplayName("호스트 없을 때 테스트")
    void testGetHostWhenNoHost(){
        project.setUsers(Arrays.asList(user1));
        assertThat(project.getHost()).isNull();
    }
}
