package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.exceptions.CustomException;
import com.syncd.exceptions.ErrorInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreateProjectUsecaseTest {
    @Mock
    private CreateProjectUsecase createProjectUsecase;

    String userId = "user123";
    String userName = "user";
    String name = "New Project";
    String description = "Description of new project";
    MockMultipartFile img = new MockMultipartFile("img", "path.png", "image/png", "Image data".getBytes());

    @Test
    void testCreateProject() {
        List<String> users = Arrays.asList("user1", "user2");
        CreateProjectResponseDto response = new CreateProjectResponseDto("proj123");
        when(createProjectUsecase.createProject(userId,userName, name, description, img, users)).thenReturn(response);

        // Execution
        CreateProjectResponseDto result = createProjectUsecase.createProject(userId,userName, name, description, img, users);

        // Verification
        assertEquals("proj123", result.projectId());
        verify(createProjectUsecase).createProject(userId,userName, name, description, img, users);
    }

    @Test
    void testCreateProjectWhenServiceThrowsException() {
        List<String> users = Arrays.asList("user1", "user2");
        when(createProjectUsecase.createProject(userId,userName, name, description, img, users))
                .thenThrow(new IllegalStateException("Database error"));

        assertThrows(IllegalStateException.class, () -> {
            createProjectUsecase.createProject(userId,userName, name, description, img, users);
        });
    }

    @Test
    void testCreateProjectWhenUserNotFoundException() {
        List<String> users = Arrays.asList("invalidUser@example.com");

        when(createProjectUsecase.createProject(userId, userName, name, description, img, users))
                .thenThrow(new CustomException(ErrorInfo.USER_NOT_FOUND, "user id : " + userId));

        // Execution and Verification
        assertThrows(CustomException.class, () -> {
            createProjectUsecase.createProject(userId, userName, name, description, img, users);
        });
    }

    @Test
    void testCreateProjectWhenProjectAlreadyExistsException() {
        List<String> users = Arrays.asList("user1@example.com");

        when(createProjectUsecase.createProject(userId, userName, name, description, img, users))
                .thenThrow(new CustomException(ErrorInfo.PROJECT_ALREADY_EXISTS, ""));

        // Execution and Verification
        assertThrows(CustomException.class, () -> {
            createProjectUsecase.createProject(userId, userName, name, description, img, users);
        });
    }
}
