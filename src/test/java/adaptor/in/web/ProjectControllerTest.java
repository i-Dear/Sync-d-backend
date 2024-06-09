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
        verifyCreateProjectUsecase("userId", "username", "Valid Project", "Description", requestDto.img(), requestDto.userEmails());
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
        when(inviteUserInProjectUsecase.inviteUserInProject(any(), any(), any(), any())).thenReturn(responseDto);

        InviteUserInProjectResponseDto response = projectController.inviteUser(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");

        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> userEmailsCaptor = ArgumentCaptor.forClass(List.class);

        verify(inviteUserInProjectUsecase).inviteUserInProject(userIdCaptor.capture(), any(), projectIdCaptor.capture(), userEmailsCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo("userId");
        assertThat(projectIdCaptor.getValue()).isEqualTo("validProjectId");
        assertThat(userEmailsCaptor.getValue()).isEqualTo(requestDto.users());
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
        verifyWithdrawUserUsecase("userId", "validProjectId", requestDto.users());
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

    @Test
    @DisplayName("Update Project - Valid Request")
    void testUpdateProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto("validProjectId", "newName", "newDesc", null);

        setupMockJwtService(request, "token", "userId", null);

        // ArgumentCaptor를 생성하여 메서드 호출시 전달되는 인자들을 캡처합니다.
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> fileCaptor = ArgumentCaptor.forClass(String.class);

        UpdateProjectResponseDto responseDto = new UpdateProjectResponseDto("validProjectId");
        when(updateProjectUsecase.updateProject(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(responseDto);

        UpdateProjectResponseDto response = projectController.updateProject(request, requestDto);

        assertThat(response.projectId()).isEqualTo("validProjectId");

        // 메서드 호출시 전달된 인자들을 캡처합니다.
        verify(updateProjectUsecase).updateProject(
                userIdCaptor.capture(), projectIdCaptor.capture(), nameCaptor.capture(),
                descriptionCaptor.capture(), fileCaptor.capture()
        );

        // 캡처한 값을 출력합니다.
        System.out.println("User ID: " + userIdCaptor.getValue());
        System.out.println("Project ID: " + projectIdCaptor.getValue());
        System.out.println("Name: " + nameCaptor.getValue());
        System.out.println("Description: " + descriptionCaptor.getValue());
        System.out.println("File: " + fileCaptor.getValue());

        verifyJwtServiceInteraction(request, "token");
        verifyUpdateProjectUsecase("userId", "validProjectId", "newName", "newDesc", null);
    }


    // ======================================
    // SyncProject
    // ======================================

    @Test
    @DisplayName("Sync Project - Valid Request")
    void testSyncProject_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile whyWhatHowImage = mock(MultipartFile.class);
        MultipartFile businessModelImage = mock(MultipartFile.class);
        MultipartFile menuTreeImage = mock(MultipartFile.class);

        SyncProjectRequestDto requestDto = new SyncProjectRequestDto(
                "validProjectId",
                10,
                "problem",
                "coreDetailsJsonString",
                "epicsJsonString",
                "personalinfosString",
                whyWhatHowImage,
                businessModelImage,
                menuTreeImage
        );

        setupMockJwtService(request, "token", "userId", null);

        SyncProjectResponseDto responseDto = new SyncProjectResponseDto("validProjectId");
        when(syncProjectUsecase.syncProject(any(), any(), anyInt(), any(),
                any(), any(MultipartFile.class), any(),
                any(MultipartFile.class), any(), any(MultipartFile.class))).thenReturn(responseDto);

        SyncProjectResponseDto response = projectController.syncProject(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("validProjectId");

        verifyJwtServiceInteraction(request, "token");
        verifySyncProjectUsecase("userId", "validProjectId", 10, "problem",
                "personalinfosString", whyWhatHowImage, "coreDetailsJsonString",
                businessModelImage, "epicsJsonString", menuTreeImage);
    }

    // ======================================
    // MakeUserstory
    // ======================================

    @Test
    @DisplayName("Make Userstory - Valid Request")
    void testMakeUserstory_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("validProjectId");
        requestDto.setScenario(List.of("Scenario 1"));

        setupMockJwtService(request, "token", "userId", "username");

        MakeUserStoryResponseDto responseDto = new MakeUserStoryResponseDto();

        when(makeUserstoryUsecase.makeUserstory(any(), any(), any())).thenReturn(responseDto);

        ResponseEntity<MakeUserStoryResponseDto> response = projectController.makeUserStory(request, requestDto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(responseDto);

        verifyJwtServiceInteraction(request, "token");

        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> storyCaptor = ArgumentCaptor.forClass(List.class);

        verify(makeUserstoryUsecase).makeUserstory(userIdCaptor.capture(), projectIdCaptor.capture(), storyCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo("userId");
        assertThat(projectIdCaptor.getValue()).isEqualTo("validProjectId");
        assertThat(storyCaptor.getValue()).isEqualTo(List.of("Scenario 1"));
    }


    // 공통 메서드
    private void setupMockJwtService(HttpServletRequest request, String token, String userId, String username) {
        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenReturn(userId);
        if (username != null) {
            when(jwtService.getUsernameFromToken(token)).thenReturn(username);
        }
    }

    private void verifyJwtServiceInteraction(HttpServletRequest request, String token) {
        verify(jwtService).resolveToken(request);
        verify(jwtService).getUserIdFromToken(token);
    }

    private void verifyCreateProjectUsecase(String expectedUserId, String expectedUsername, String expectedName, String expectedDescription, MultipartFile expectedFile, List<String> expectedUserEmails) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
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

    private void verifyWithdrawUserUsecase(String expectedUserId, String expectedProjectId, List<String> expectedUserEmails) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> userEmailsCaptor = ArgumentCaptor.forClass(List.class);

        verify(withdrawUserInProjectUsecase).withdrawUserInProject(userIdCaptor.capture(), projectIdCaptor.capture(), userEmailsCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(userEmailsCaptor.getValue()).isEqualTo(expectedUserEmails);
    }

    private void verifyDeleteProjectUsecase(String expectedUserId, String expectedProjectId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(deleteProjectUsecase).deleteProject(userIdCaptor.capture(), projectIdCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
    }

    private void verifyUpdateProjectUsecase(String expectedUserId, String expectedProjectId, String expectedName, String expectedDescription, String expectedFile) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> fileCaptor = ArgumentCaptor.forClass(String.class);

        verify(updateProjectUsecase).updateProject(userIdCaptor.capture(), projectIdCaptor.capture(), nameCaptor.capture(), descriptionCaptor.capture(), fileCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(nameCaptor.getValue()).isEqualTo(expectedName);
        assertThat(descriptionCaptor.getValue()).isEqualTo(expectedDescription);
        assertThat(fileCaptor.getValue()).isEqualTo(expectedFile);
    }

    private void verifySyncProjectUsecase(String expectedUserId, String expectedProjectId, int expectedVersion, String expectedProblem, String expectedPersonalInfos, MultipartFile expectedWhyWhatHowImage, String expectedCoreDetails, MultipartFile expectedBusinessModelImage, String expectedEpics, MultipartFile expectedMenuTreeImage) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> projectIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> versionCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> problemCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> personalInfosCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> whyWhatHowImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<String> coreDetailsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> businessModelImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<String> epicsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MultipartFile> menuTreeImageCaptor = ArgumentCaptor.forClass(MultipartFile.class);

        verify(syncProjectUsecase).syncProject(userIdCaptor.capture(), projectIdCaptor.capture(), versionCaptor.capture(), problemCaptor.capture(), personalInfosCaptor.capture(), whyWhatHowImageCaptor.capture(), coreDetailsCaptor.capture(), businessModelImageCaptor.capture(), epicsCaptor.capture(), menuTreeImageCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(expectedUserId);
        assertThat(projectIdCaptor.getValue()).isEqualTo(expectedProjectId);
        assertThat(versionCaptor.getValue()).isEqualTo(expectedVersion);
        assertThat(problemCaptor.getValue()).isEqualTo(expectedProblem);
        assertThat(personalInfosCaptor.getValue()).isEqualTo(expectedPersonalInfos);
        assertThat(whyWhatHowImageCaptor.getValue()).isEqualTo(expectedWhyWhatHowImage);
        assertThat(coreDetailsCaptor.getValue()).isEqualTo(expectedCoreDetails);
        assertThat(businessModelImageCaptor.getValue()).isEqualTo(expectedBusinessModelImage);
        assertThat(epicsCaptor.getValue()).isEqualTo(expectedEpics);
        assertThat(menuTreeImageCaptor.getValue()).isEqualTo(expectedMenuTreeImage);
    }

}
