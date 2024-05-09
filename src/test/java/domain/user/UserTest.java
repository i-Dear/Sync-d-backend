package domain.user;

import com.syncd.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private User user1;
    private User user2;

    @BeforeEach
    void setUp(){
        user1 = new User();
        user1.setId("123");
        user1.setName("이찬주");
        user1.setEmail("이찬주@example.com");
        user1.setProfileImg("profile.jpg");

        user2 = new User();
        user2.setId("123");
        user2.setName("이찬주");
        user2.setEmail("이찬주@example.com");
        user2.setProfileImg("profile.jpg");
    }
    @Test
    @DisplayName("Getter Setter 테스트")
    public void testUserGettersAndSetters() {
        assertThat(user1.getId()).isEqualTo("123");
        assertThat(user1.getName()).isEqualTo("이찬주");
        assertThat(user1.getEmail()).isEqualTo("이찬주@example.com");
        assertThat(user1.getProfileImg()).isEqualTo("profile.jpg");
    }


    @Test
    @DisplayName("동일한 User에 대한 동등성과 해시 일관성 테스트")
    public void testUserEquality() {
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}