package application.service;

import Dummy.Consistent;
import Dummy.Stub.application.out.gmail.StubSendMailPort;
import Dummy.Stub.application.out.liveblock.StubLiveblocksPort;
import Dummy.Stub.application.out.openai.StubChatGPTPort;
import Dummy.Stub.application.out.persistence.project.StubReadProjectPort;
import Dummy.Stub.application.out.persistence.project.StubWriteProjectPort;
import Dummy.Stub.application.out.persistence.user.StubReadUserPort;
import Dummy.Stub.application.out.s3.StubS3Port;
import Dummy.domain.MockProject;
import com.syncd.application.port.in.*;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.gmail.SendMailPort;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.openai.ChatGPTPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.application.service.ProjectService;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.dto.*;
import com.syncd.enums.Role;
import com.syncd.mapper.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    private StubReadProjectPort readProjectPort;

    private StubWriteProjectPort writeProjectPort;

    private StubReadUserPort readUserPort;

    private StubLiveblocksPort liveblocksPort;

    private StubSendMailPort sendMailPort;

    private StubChatGPTPort chatGPTPort;

    private StubS3Port s3Port;

    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        readProjectPort = new StubReadProjectPort();
        writeProjectPort = new StubWriteProjectPort();
        readUserPort = new StubReadUserPort();
        liveblocksPort = new StubLiveblocksPort();
        sendMailPort = new StubSendMailPort();
        chatGPTPort = new StubChatGPTPort();
        s3Port = new StubS3Port();
        projectMapper = Mockito.mock(ProjectMapper.class);

        projectService = new ProjectService(readProjectPort, writeProjectPort, readUserPort, liveblocksPort, sendMailPort, chatGPTPort, s3Port, projectMapper);
    }


    @Test
    void createProject() {
        // Given
        String hostId = Consistent.UserId.getValue();
        String hostName = Consistent.UserName.getValue();
        String projectName = Consistent.ProjectName.getValue();
        String description = "description";
        MultipartFile img = mock(MultipartFile.class);
        List<String> userEmails = Arrays.asList("user1@example.com", "user2@example.com");

        User host = new User();
        host.setId(hostId);
        host.setName(hostName);

        Project project = new MockProject();
        project.setId(Consistent.ProjectId.getValue());

        // When
        CreateProjectUsecase.CreateProjectResponseDto response = projectService.createProject(hostId, hostName, projectName, description, img, userEmails);

        // Then
        assertNotNull(response);
        assertEquals(Consistent.ProjectId.getValue(), response.projectId());
      }

    @Test
    void joinProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();

        Project project = new MockProject();
        project.setId(projectId);
        project.setUsers(Collections.singletonList(new UserInProject(userId, Role.MEMBER)));

        // When
        JoinProjectUsecase.JoinProjectResponseDto response = projectService.joinProject(userId, projectId);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void getAllRoomsByUserId() {
        // Given
        String userId = Consistent.UserId.getValue();

        Project project = new MockProject();
        project.setId(Consistent.ProjectId.getValue());

        // When
        GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto response = projectService.getAllRoomsByUserId(userId);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertFalse(response.projects().isEmpty());
    }

    @Test
    void getRoomAuthToken() {
        // Given
        String userId = Consistent.UserId.getValue();

        User user = new User();
        user.setId(userId);
        user.setName(Consistent.UserName.getValue());

        Project project = new MockProject();
        project.setId(Consistent.ProjectId.getValue());

        // When
        GetRoomAuthTokenUsecase.GetRoomAuthTokenResponseDto response = projectService.getRoomAuthToken(userId);

        // Then
        assertNotNull(response);
        assertEquals(Consistent.LiveblocksToken.getValue(), response.token());
    }

    @Test
    void deleteProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();

        Project project = new MockProject();
        project.setId(projectId);
        project.setImg("https://example.com/dummyimage.jpg");

        // When
        DeleteProjectUsecase.DeleteProjectResponseDto response = projectService.deleteProject(userId, projectId);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void inviteUserInProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();
        List<String> userEmails = Arrays.asList("user1@example.com", "user2@example.com");

        Project project = new MockProject();
        project.setId(projectId);

        User host = new User();
        host.setId(userId);

        // When
        InviteUserInProjectUsecase.InviteUserInProjectResponseDto response = projectService.inviteUserInProject(userId, projectId, userEmails);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void updateProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();
        String projectName = Consistent.ProjectName.getValue();
        String description = "updatedDescription";
        String image = "updatedImage";

        Project project = new MockProject();
        project.setId(projectId);

        // When
        UpdateProjectUsecase.UpdateProjectResponseDto response = projectService.updateProject(userId, projectId, projectName, description, image);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void withdrawUserInProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();
        List<String> userIds = Arrays.asList("user1", "user2");

        Project project = new MockProject();
        project.setId(projectId);
        project.setUsers(Arrays.asList(
                new UserInProject("user1", Role.MEMBER),
                new UserInProject("user2", Role.MEMBER),
                new UserInProject("user3", Role.HOST)
        ));
        // When
        WithdrawUserInProjectUsecase.WithdrawUserInProjectResponseDto response = projectService.withdrawUserInProject(userId, projectId, userIds);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void syncProject() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();
        int projectStage = 1;

        Project project = new MockProject();
        project.setId(projectId);

        // When
        SyncProjectUsecase.SyncProjectResponseDto response = projectService.syncProject(userId, projectId, projectStage);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void makeUserstory() {
        // Given
        String userId = Consistent.UserId.getValue();
        String projectId = Consistent.ProjectId.getValue();
        List<String> scenarios = Arrays.asList("scenario1", "scenario2");

        // When
        MakeUserStoryResponseDto response = projectService.makeUserstory(userId, projectId, scenarios);

        // Then
        assertNotNull(response);
        assertEquals(new MakeUserStoryResponseDto(), response);
    }
}
