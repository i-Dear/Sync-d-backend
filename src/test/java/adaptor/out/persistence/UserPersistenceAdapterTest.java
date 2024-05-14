package adaptor.out.persistence;

import com.syncd.adapter.out.persistence.UserPersistenceAdapter;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.domain.user.User;
import com.syncd.dto.UserId;
import com.syncd.enums.UserAccountStatus;
import com.syncd.exceptions.CustomException;
import com.syncd.exceptions.ErrorInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserPersistenceAdapterTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;
    private UserEntity userEntity;
    @BeforeEach
    void setUp(){
        userEntity = new UserEntity();
        userEntity.setId("123");
        userEntity.setName("John Doe");
        userEntity.setEmail("johndoe@example.com");
        userEntity.setProfileImg("profile.jpg");
        userEntity.setStatus(UserAccountStatus.AVAILABLE);
    }
    @Test
    void createUserTest() {
        when(userDao.save(any(UserEntity.class))).thenReturn(userEntity);

        UserId result = userPersistenceAdapter.createUser("John Doe", "johndoe@example.com", "profile.jpg");

        assertNotNull(result);
        assertEquals("123", result.value());
        verify(userDao).save(any(UserEntity.class));
    }

    @Test
    void findByEmailTest() {
        when(userDao.findByEmail("johndoe@example.com")).thenReturn(Optional.of(userEntity));

        User result = userPersistenceAdapter.findByEmail("johndoe@example.com");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void findByEmailNotFoundTest() {
        when(userDao.findByEmail("johndoe@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomException.class, () ->
                userPersistenceAdapter.findByEmail("johndoe@example.com")
        );

        String expectedMessage = ErrorInfo.USER_NOT_FOUND.getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void findByUsernameTest() {
        when(userDao.findByName("John Doe")).thenReturn(Optional.of(userEntity));

        User result = userPersistenceAdapter.findByUsername("John Doe");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void isExistUserTest() {
        when(userDao.findByEmail("johndoe@example.com")).thenReturn(Optional.of(new UserEntity()));

        boolean exists = userPersistenceAdapter.isExistUser("johndoe@example.com");

        assertTrue(exists);
    }
}
