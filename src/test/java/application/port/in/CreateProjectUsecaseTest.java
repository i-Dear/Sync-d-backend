package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.exceptions.ProjectAlreadyExistsException;
import com.syncd.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreateProjectUsecaseTest {
    @Mock
    private CreateProjectUsecase createProjectUsecase;
    @Test
    void testCreateProject() {
        // Setup
        String userId = "user123";
        String userName = "user";
        String name = "New Project";
        String description = "Description of new project";
        String img = "image/path.png";
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
        String userId = "user123";
        String userName = "user";
        String name = "New Project";
        String description = "Description of new project";
        String img = "image/path.png";
        List<String> users = Arrays.asList("user1", "user2");

        when(createProjectUsecase.createProject(userId,userName, name, description, img, users))
                .thenThrow(new IllegalStateException("Database error"));

        assertThrows(IllegalStateException.class, () -> {
            createProjectUsecase.createProject(userId,userName, name, description, img, users);
        });
    }

    @Test
    void testCreateProjectWhenUserNotFoundException() {
        // Setup
        String userId = "user123";
        String userName = "user";
        String name = "New Project";
        String description = "Description of new project";
        String img = "image/path.png";
        List<String> users = Arrays.asList("invalidUser@example.com");

        when(createProjectUsecase.createProject(userId, userName, name, description, img, users))
                .thenThrow(new UserNotFoundException("User not found for email: invalidUser@example.com"));

        // Execution and Verification
        assertThrows(UserNotFoundException.class, () -> {
            createProjectUsecase.createProject(userId, userName, name, description, img, users);
        });
    }

    @Test
    void testCreateProjectWhenProjectAlreadyExistsException() {
        // Setup
        String userId = "user123";
        String userName = "user";
        String name = "Existing Project";
        String description = "This project already exists";
        String img = "image/path.png";
        List<String> users = Arrays.asList("user1@example.com");

        when(createProjectUsecase.createProject(userId, userName, name, description, img, users))
                .thenThrow(new ProjectAlreadyExistsException("Project already exists with name: Existing Project"));

        // Execution and Verification
        assertThrows(ProjectAlreadyExistsException.class, () -> {
            createProjectUsecase.createProject(userId, userName, name, description, img, users);
        });
    }
}
