package application.port.out.persistence.user;

import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class  ReadUserPortTest{
    private ReadUserPort readUserPort = Mockito.mock(ReadUserPort.class);

    @Test
    void findByEmailShouldReturnCorrectUser() {
        User user = new User();
        user.setId("1");
        user.setName("User Name");
        user.setEmail("user@example.com");
        user.setProfileImg("profileImg.jpg");
        when(readUserPort.findByEmail("user@example.com")).thenReturn(user);

        User foundUser = readUserPort.findByEmail("user@example.com");
        assertNotNull(foundUser, "findByEmail should return a non-null user");
        assertEquals("user@example.com", foundUser.getEmail(), "The user should have the correct email");
    }

    @Test
    void findByUsernameShouldReturnCorrectUser() {
        User user = new User();
        user.setId("2");
        user.setName("Another User");
        user.setEmail("another@example.com");
        user.setProfileImg("anotherProfile.jpg");

        when(readUserPort.findByUsername("Another User")).thenReturn(user);

        User foundUser = readUserPort.findByUsername("Another User");
        assertNotNull(foundUser, "findByUsername should return a non-null user");
        assertEquals("Another User", foundUser.getName(), "The user should have the correct username");
    }

    @Test
    void findByUserIdShouldReturnCorrectUser() {
        User user = new User();
        user.setId("1");
        user.setName("User Name");
        user.setEmail("user@example.com");
        user.setProfileImg("profileImg.jpg");
        when(readUserPort.findByUserId("1")).thenReturn(user);

        User foundUser = readUserPort.findByUserId("1");
        assertNotNull(foundUser, "findByUserId should return a non-null user");
        assertEquals("1", foundUser.getId(), "The user should have the correct user ID");
    }

    @Test
    void isExistUserShouldReturnTrueForExistingUser() {
        when(readUserPort.isExistUser("user@example.com")).thenReturn(true);

        boolean exists = readUserPort.isExistUser("user@example.com");
        assertTrue(exists, "isExistUser should return true for an existing user");
    }

    @Test
    void isExistUserShouldReturnFalseForNonExistingUser() {
        when(readUserPort.isExistUser("nonexistent@example.com")).thenReturn(false);

        boolean exists = readUserPort.isExistUser("nonexistent@example.com");
        assertFalse(exists, "isExistUser should return false for a non-existing user");
    }
}
