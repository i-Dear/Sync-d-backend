package application.service;

import com.syncd.adapter.out.persistence.exception.ProjectAlreadyExistsException;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;

import com.syncd.application.service.ProjectService;
import com.syncd.domain.project.Project;
import com.syncd.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ReadProjectPort readProjectPort;
    @Mock
    private WriteProjectPort writeProjectPort;
    @Mock
    private ReadUserPort readUserPort;
    @Mock
    private LiveblocksPort liveblocksPort;
    @InjectMocks
    private ProjectService projectService;

    private final String userId = "user1";
    private final String projectId = "project1";
    private Project project;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("민수");
        user.setEmail("민수@example.com");
        user.setProfileImg("img.png");

        project = new Project(projectId);
        project.setDescription("Description");
        project.setName("Title");
        project.setImg("img.png");
        project.setUsers(new ArrayList<>());

        when(readUserPort.findByUserId(anyString())).thenReturn(user);
        when(readProjectPort.findProjectByProjectId(anyString())).thenReturn(project);
        when(writeProjectPort.CreateProject(any(Project.class))).thenReturn(projectId);
    }
    @Test
    void testCreateProject_Success() {
        List<String> userIds = List.of("2", "3");
        CreateProjectResponseDto response = projectService.createProject(userId, "New Project", "New Description", "image.png", userIds);
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void testCreateProject_Failure_UserNotFound() {
        when(readUserPort.findByUserId(userId)).thenReturn(null);
        assertThrows(ProjectAlreadyExistsException.class, () ->
                projectService.createProject(userId, "New Project", "New Description", "image.png", List.of("2", "3"))
        );
    }

    @Test
    void testUpdateProject_Success() {
        UpdateProjectResponseDto response = projectService.updateProject(userId, projectId, "Updated Name", "Updated Description", "updated.png");
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void testUpdateProject_Failure_NotHost() {
        String anotherUserId = "user2";
        assertThrows(ProjectAlreadyExistsException.class, () ->
                projectService.updateProject(anotherUserId, projectId, "Updated Name", "Updated Description", "updated.png")
        );
    }

    @Test
    void testDeleteProject_Success() {
        DeleteProjectResponseDto response = projectService.deleteProject(userId, projectId);
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void testDeleteProject_Failure_NotAuthorized() {
        String anotherUserId = "user2";
        assertThrows(ProjectAlreadyExistsException.class, () ->
                projectService.deleteProject(anotherUserId, projectId)
        );
    }

    @Test
    void testGetAllRoomsByUserId() {
        GetAllRoomsByUserIdResponseDto response = projectService.getAllRoomsByUserId(userId);
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertFalse(response.projects().isEmpty());
    }

    @Test
    void testInviteUserInProject_Success() {
        List<String> userIds = List.of("2", "3");
        InviteUserInProjectResponseDto response = projectService.inviteUserInProject(userId, projectId, userIds);
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void testWithdrawUserInProject_Success() {
        List<String> userIds = List.of("2", "3");
        WithdrawUserInProjectResponseDto response = projectService.withdrawUserInProject(userId, projectId, userIds);
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void testGetRoomAuthToken_Success() {
        GetRoomAuthTokenResponseDto response = projectService.getRoomAuthToken(userId);
        assertNotNull(response);
        assertNotNull(response.token());
    }
}
