package adaptor.in.web;

import com.syncd.adapter.in.web.ProjectController;
import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.JoinProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.SyncProjectUsecase.*;
import com.syncd.application.service.JwtService;
import com.syncd.domain.project.CoreDetails;
import com.syncd.domain.project.Epic;
import com.syncd.dto.MakeUserStoryReauestDto;
import com.syncd.dto.MakeUserStoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {

    @Mock
    private CreateProjectUsecase createProjectUsecase;

    @Mock
    private JoinProjectUsecase joinProjectUsecase;

    @Mock
    private InviteUserInProjectUsecase inviteUserInProjectUsecase;

    @Mock
    private WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;

    @Mock
    private DeleteProjectUsecase deleteProjectUsecase;

    @Mock
    private UpdateProjectUsecase updateProjectUsecase;

    @Mock
    private SyncProjectUsecase syncProjectUsecase;

    @Mock
    private MakeUserstoryUsecase makeUserstoryUsecase;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======================================
    // CreateProject
    // ======================================

    @Test
    @DisplayName("Create Project - Valid Request")
    void testCreateProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto("Valid Project", "Description", new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes()), Collections.emptyList());

        setupMockJwtService(request, "token", "userId", "username");

        CreateProjectResponseDto responseDto = new CreateProjectResponseDto("projectId");
        when(createProjectUsecase.createProject(anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn(responseDto);

        CreateProjectResponseDto response = projectController.createProject(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("projectId");

        verifyJwtServiceInteraction(request, "token");
        verifyCreateProjectUsecase("userId", "username", "Valid Project", "Description", new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes()), Collections.emptyList());
    }

    // ======================================
    // JoinProject
    // ======================================

    @Test
    @DisplayName("Join Project - Valid Request")
    void testJoinProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        JoinProjectRequestDto requestDto = new JoinProjectRequestDto("validProjectId");

        setupMockJwtService(request, "token", "userId", null);

        JoinProjectResponseDto responseDto = new JoinProjectResponseDto("validProjectId");
        when(joinProjectUsecase.joinProject(anyString(), anyString())).thenReturn(responseDto);

        JoinProjectResponseDto response = projectController.joinProject(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifyJoinProjectUsecase("userId", "validProjectId");
    }

    // ======================================
    // InviteUser
    // ======================================

    @Test
    @DisplayName("Invite User - Valid Request")
    void testInviteUser_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("validProjectId", List.of("user1@example.com"));

        setupMockJwtService(request, "token", "userId", null);

        InviteUserInProjectResponseDto responseDto = new InviteUserInProjectResponseDto("validProjectId");
        when(inviteUserInProjectUsecase.inviteUserInProject(anyString(),any(), anyString(), any())).thenReturn(responseDto);

        InviteUserInProjectResponseDto response = projectController.inviteUser(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifyInviteUserUsecase("userId", "validProjectId", List.of("user1@example.com"));
    }

    // ======================================
    // WithdrawUser
    // ======================================

    @Test
    @DisplayName("Withdraw User - Valid Request")
    void testWithdrawUser_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("validProjectId", List.of("user1@example.com"));

        setupMockJwtService(request, "token", "userId", null);

        WithdrawUserInProjectResponseDto responseDto = new WithdrawUserInProjectResponseDto("validProjectId");
        when(withdrawUserInProjectUsecase.withdrawUserInProject(anyString(), anyString(), any())).thenReturn(responseDto);

        WithdrawUserInProjectResponseDto response = projectController.withdrawUser(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifyWithdrawUserUsecase("userId", "validProjectId", List.of("user1@example.com"));
    }

    // ======================================
    // DeleteProject
    // ======================================

    @Test
    @DisplayName("Delete Project - Valid Request")
    void testDeleteProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        DeleteProjectRequestDto requestDto = new DeleteProjectRequestDto("validProjectId");

        setupMockJwtService(request, "token", "userId", null);

        DeleteProjectResponseDto responseDto = new DeleteProjectResponseDto("validProjectId");
        when(deleteProjectUsecase.deleteProject(anyString(), anyString())).thenReturn(responseDto);

        DeleteProjectResponseDto response = projectController.deleteProject(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifyDeleteProjectUsecase("userId", "validProjectId");
    }

    // ======================================
    // UpdateProject
    // ======================================
//    TODO: when 후에 return 되는 responseDto가 null, 억까임
//    @Test
//    @DisplayName("Update Project - Valid Request")
//    void testUpdateProject_ValidRequest() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto("validProjectId", "newName", "newDesc", null);
//
//        setupMockJwtService(request, "token", "userId", null);
//
//        UpdateProjectResponseDto responseDto = new UpdateProjectResponseDto("validProjectId");
//        when(updateProjectUsecase.updateProject(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(responseDto);
//
//        UpdateProjectResponseDto response = projectController.updateProject(request, requestDto);
//
//        assertThat(response).isNotNull();
//        assertThat(response.projectId()).isEqualTo("validProjectId");
//
//        verifyJwtServiceInteraction(request, "token");
//        verifyUpdateProjectUsecase("userId", "validProjectId", "newName", "newDesc", null);
//    }

    // ======================================
    // SyncProject
    // ======================================

    @Test
    @DisplayName("Sync Project - Valid Request")
    void testSyncProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile personaImage = mock(MultipartFile.class);
        MultipartFile whyWhatHowImage = mock(MultipartFile.class);
        MultipartFile businessModelImage = mock(MultipartFile.class);
        MultipartFile menuTreeImage = mock(MultipartFile.class);

        SyncProjectRequestDto requestDto = new SyncProjectRequestDto(
                "validProjectId",
                10,
                "problem",
                "coreDetailsJsonString",
                "epicsJsonString",
                personaImage,
                whyWhatHowImage,
                businessModelImage,
                menuTreeImage
        );

        setupMockJwtService(request, "token", "userId", null);

        SyncProjectResponseDto responseDto = new SyncProjectResponseDto("validProjectId");
        when(syncProjectUsecase.syncProject(anyString(), anyString(), anyInt(), anyString(),
                any(MultipartFile.class), any(MultipartFile.class), anyString(),
                any(MultipartFile.class), anyString(), any(MultipartFile.class))).thenReturn(responseDto);

        SyncProjectResponseDto response = projectController.syncProject(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifySyncProjectUsecase("userId", "validProjectId", 10, "problem",
                personaImage, whyWhatHowImage, "coreDetailsJsonString",
                businessModelImage, "epicsJsonString", menuTreeImage);
    }


    // ======================================
    // MakeUserStory
    // ======================================

    @Test
    @DisplayName("Make User Story - Valid Request")
    void testMakeUserStory_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("validProjectId");
        requestDto.setScenario(List.of("Scenario 1"));

        setupMockJwtService(request, "token", "userId", null);

        MakeUserStoryResponseDto responseDto = new MakeUserStoryResponseDto();
        when(makeUserstoryUsecase.makeUserstory(anyString(), anyString(), any())).thenReturn(responseDto);

        ResponseEntity<MakeUserStoryResponseDto> response = projectController.makeUserStory(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(responseDto);

        verifyJwtServiceInteraction(request, "token");
        verifyMakeUserstoryUsecase("userId", "validProjectId", List.of("Scenario 1"));
    }

    private void setupMockJwtService(HttpServletRequest request, String token, String userId, String username) {
        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenReturn(userId);
        when(jwtService.getUsernameFromToken(token)).thenReturn(username);
    }

    private void verifyJwtServiceInteraction(HttpServletRequest request, String token) {
        verify(jwtService).resolveToken(request);
        verify(jwtService).getUserIdFromToken(token);
    }

    private void verifyCreateProjectUsecase(String expectedUserId, String expectedUsername, String expectedName, String expectedDescription, MockMultipartFile expectedFile, List<String> expectedUserEmails) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MockMultipartFile> fileCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);
        ArgumentCaptor<List<String>> userEmailsCaptor = ArgumentCaptor.forClass(List.class);

        verify(createProjectUsecase).createProject(userIdCaptor.capture(), usernameCaptor.capture(), nameCaptor.capture(), descriptionCaptor.capture(), fileCaptor.capture(), userEmailsCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(usernameCaptor.getValue()).isEqualTo(expectedUsername);
        assertThat(nameCaptor.getValue()).isEqualTo(expectedName);
        assertThat(descriptionCaptor.getValue()).isEqualTo(expectedDescription);
        assertThat(fileCaptor.getValue()).isNotNull();
        assertThat(userEmailsCaptor.getValue()).isEqualTo(expectedUserEmails);
    }

    private void verifyJoinProjectUsecase(String expectedUserId, String expectedProjectId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(joinProjectUsecase).joinProject(userIdCaptor.capture(), projectIdCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
    }

    private void verifyInviteUserUsecase(String expectedUserId, String expectedProjectId, List<String> expectedUsers) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> usersCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);

        verify(inviteUserInProjectUsecase).inviteUserInProject(userIdCaptor.capture(),userNameCaptor.capture(), projectIdCaptor.capture(), usersCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(usersCaptor.getValue()).isEqualTo(expectedUsers);
    }

    private void verifyWithdrawUserUsecase(String expectedUserId, String expectedProjectId, List<String> expectedUsers) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> usersCaptor = ArgumentCaptor.forClass(List.class);

        verify(withdrawUserInProjectUsecase).withdrawUserInProject(userIdCaptor.capture(), projectIdCaptor.capture(), usersCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(usersCaptor.getValue()).isEqualTo(expectedUsers);
    }

    private void verifyDeleteProjectUsecase(String expectedUserId, String expectedProjectId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(deleteProjectUsecase).deleteProject(userIdCaptor.capture(), projectIdCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
    }

    private void verifyUpdateProjectUsecase(String expectedUserId, String expectedProjectId, String expectedProjectName, String expectedDescription, String expectedImage) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> imageCaptor = ArgumentCaptor.forClass(String.class);

        verify(updateProjectUsecase).updateProject(userIdCaptor.capture(), projectIdCaptor.capture(), projectNameCaptor.capture(), descriptionCaptor.capture(), imageCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(projectNameCaptor.getValue()).isEqualTo(expectedProjectName);
        assertThat(descriptionCaptor.getValue()).isEqualTo(expectedDescription);
        assertThat(imageCaptor.getValue()).isEqualTo(expectedImage);
    }

    private void verifySyncProjectUsecase(String expectedUserId, String expectedProjectId, int expectedProjectStage,
                                          String expectedProblem, MultipartFile expectedPersonaImage,
                                          MultipartFile expectedWhyWhatHowImage, String expectedCoreDetailsJson,
                                          MultipartFile expectedBusinessModelImage, String expectedEpicsJson,
                                          MultipartFile expectedMenuTreeImage) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> projectStageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> problemCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> personaImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<MultipartFile> whyWhatHowImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<String> coreDetailsJsonCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> businessModelImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<String> epicsJsonCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> menuTreeImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);

        verify(syncProjectUsecase).syncProject(userIdCaptor.capture(), projectIdCaptor.capture(), projectStageCaptor.capture(),
                problemCaptor.capture(), personaImageCaptor.capture(), whyWhatHowImageCaptor.capture(), coreDetailsJsonCaptor.capture(),
                businessModelImageCaptor.capture(), epicsJsonCaptor.capture(), menuTreeImageCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(projectStageCaptor.getValue()).isEqualTo(expectedProjectStage);
        assertThat(problemCaptor.getValue()).isEqualTo(expectedProblem);
        assertThat(personaImageCaptor.getValue()).isEqualTo(expectedPersonaImage);
        assertThat(whyWhatHowImageCaptor.getValue()).isEqualTo(expectedWhyWhatHowImage);
        assertThat(coreDetailsJsonCaptor.getValue()).isEqualTo(expectedCoreDetailsJson);
        assertThat(businessModelImageCaptor.getValue()).isEqualTo(expectedBusinessModelImage);
        assertThat(epicsJsonCaptor.getValue()).isEqualTo(expectedEpicsJson);
        assertThat(menuTreeImageCaptor.getValue()).isEqualTo(expectedMenuTreeImage);
    }
    private void verifyMakeUserstoryUsecase(String expectedUserId, String expectedProjectId, List<String> expectedScenario) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> scenarioCaptor = ArgumentCaptor.forClass(List.class);

        verify(makeUserstoryUsecase).makeUserstory(userIdCaptor.capture(), projectIdCaptor.capture(), scenarioCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(scenarioCaptor.getValue()).isEqualTo(expectedScenario);
    }
}
