package application.service;

import Dummy.ProjectDummyData;
import Dummy.Stub.application.out.gmail.StubSendMailPort;
import Dummy.Stub.application.out.liveblock.StubLiveblocksPort;
import Dummy.Stub.application.out.openai.StubChatGPTPort;
import Dummy.Stub.application.out.persistence.project.StubReadProjectPort;
import Dummy.Stub.application.out.persistence.project.StubWriteProjectPort;
import Dummy.Stub.application.out.persistence.user.StubReadUserPort;
import Dummy.Stub.application.out.s3.StubS3Port;
import Dummy.domain.StubProject;
import com.syncd.application.port.in.*;
import com.syncd.application.service.ProjectService;
import com.syncd.domain.project.Project;
import com.syncd.domain.user.User;
import com.syncd.dto.*;
import com.syncd.mapper.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        Project stubProject = new StubProject();
        MultipartFile img = mock(MultipartFile.class);

        // When
        CreateProjectUsecase.CreateProjectResponseDto response = projectService.createProject(ProjectDummyData.HostId.getValue(), ProjectDummyData.HostName.getValue(), ProjectDummyData.ProjectName.getValue(), ProjectDummyData.ProjectDescription.getValue(), img, ProjectDummyData.getUserLists());

        // Then
        assertNotNull(response);
        assertEquals(stubProject.getId(), response.projectId());
      }

    @Test
    void joinProject() {
        // Given
        String userId = ProjectDummyData.User1Id.getValue();
        String projectId = ProjectDummyData.ProjectId.getValue();

        // When
        JoinProjectUsecase.JoinProjectResponseDto response = projectService.joinProject(userId, projectId);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void getAllRoomsByUserId() {
        // Given
        String userId = ProjectDummyData.User1Id.getValue();

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
        String userId = ProjectDummyData.User1Id.getValue();

        User user = new User();
        user.setId(userId);
        user.setName(ProjectDummyData.User1Name.getValue());

        // When
        GetRoomAuthTokenUsecase.GetRoomAuthTokenResponseDto response = projectService.getRoomAuthToken(userId);

        // Then
        assertNotNull(response);
        assertEquals(ProjectDummyData.LiveblocksToken.getValue(), response.token());
    }

    @Test
    void deleteProject() {
        // Given
        String userId = ProjectDummyData.User1Id.getValue();
        String projectId = ProjectDummyData.ProjectId.getValue();

        // When
        DeleteProjectUsecase.DeleteProjectResponseDto response = projectService.deleteProject(userId, projectId);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void inviteUserInProject() {
        // Given
        Project stubProject = new StubProject();
        List<String> invitedUsers = ProjectDummyData.getUserLists();

        // When
        InviteUserInProjectUsecase.InviteUserInProjectResponseDto response = projectService.inviteUserInProject(stubProject.getHost(),stubProject.getName(), stubProject.getId(), invitedUsers);

        // Then
        assertNotNull(response);
        assertEquals(stubProject.getId(), response.projectId());
    }

    @Test
    void updateProject() {
        // Given
        Project stubProject = new StubProject();

        // When
        UpdateProjectUsecase.UpdateProjectResponseDto response = projectService.updateProject(stubProject.getHost(), stubProject.getId(), stubProject.getName(), stubProject.getDescription(), stubProject.getDescription());

        // Then
        assertNotNull(response);
        assertEquals(stubProject.getId(), response.projectId());
    }

    @Test
    void withdrawUserInProject() {
        // Given
        Project stubProject = new StubProject();
        List<String> withdrawedUserIds = ProjectDummyData.getUserLists();

        // When
        WithdrawUserInProjectUsecase.WithdrawUserInProjectResponseDto response = projectService.withdrawUserInProject(stubProject.getHost(), stubProject.getId(), withdrawedUserIds);

        // Then
        assertNotNull(response);
        assertEquals(stubProject.getId(), response.projectId());
    }

    @Test
    void syncProject() {
        // Given
        String userId = ProjectDummyData.User1Id.getValue();
        String projectId = ProjectDummyData.ProjectId.getValue();
        int projectStage = 1;
        MultipartFile mockFile = mock(MultipartFile.class);
        // When
        SyncProjectUsecase.SyncProjectResponseDto response = projectService.syncProject(
                userId, projectId, projectStage,
                "problem",
                "[]", mockFile, "{}", mockFile,
                "[]", mockFile
        );
        // Then
        assertNotNull(response);
        assertEquals(projectId, response.projectId());
    }

    @Test
    void makeUserstory() {
        // Given
        String userId = ProjectDummyData.User1Id.getValue();
        String projectId = ProjectDummyData.ProjectId.getValue();
        List<String> scenarios = Arrays.asList("scenario1", "scenario2");

        // When
        MakeUserStoryResponseDto response = projectService.makeUserstory(userId, projectId, scenarios);

        // Then
        assertNotNull(response);
        assertEquals(new MakeUserStoryResponseDto(), response);
    }
}
