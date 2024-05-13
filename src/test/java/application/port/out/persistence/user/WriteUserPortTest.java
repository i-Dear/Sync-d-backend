package application.port.out.persistence.user;

import com.syncd.application.port.out.persistence.user.WriteUserPort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.syncd.domain.user.User;
import com.syncd.dto.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class WriteUserPortTest {
    private WriteUserPort writeUserPort = Mockito.mock(WriteUserPort.class);

    @Test
    void createUserShouldReturnValidUserId() {
        when(writeUserPort.createUser("Alice", "alice@example.com", "image.jpg")).thenReturn(new UserId("1"));

        UserId userId = writeUserPort.createUser("Alice", "alice@example.com", "image.jpg");
        assertNotNull(userId, "createUser should return a non-null UserId");
        assertEquals("1", userId.value(), "The UserId should contain the correct ID");
    }

    @Test
    void updateUserShouldReturnUpdatedUserId() {
        User user = new User();
        user.setId("1");
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setProfileImg("image.jpg");

        when(writeUserPort.updateUser(any(User.class))).thenReturn(new UserId("1"));

        UserId updatedUserId = writeUserPort.updateUser(user);
        assertNotNull(updatedUserId, "updateUser should return a non-null UserId");
        assertEquals("1", updatedUserId.value(), "The updated UserId should contain the correct ID");
    }
}
