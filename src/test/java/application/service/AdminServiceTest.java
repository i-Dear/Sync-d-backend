package application.service;

import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.admin.*;
import com.syncd.application.service.AdminService;
import com.syncd.enums.UserAccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Project - Valid Request")
    void testCreateProject_ValidRequest() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId("testProjectId");
        when(projectDao.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        CreateProjectAdminUsecase.CreateProjectAdminResponseDto response = adminService.createProject("Test Project", "Description", "imgUrl",
                Collections.emptyList(), 50, 5);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("testProjectId");
    }

    @Test
    @DisplayName("Create User - Valid Request")
    void testCreateUser_ValidRequest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testUserId");
        when(userDao.save(any(UserEntity.class))).thenReturn(userEntity);

        CreateUserAdminUsecase.CreateUserResponseDto response = adminService.addUser("test@example.com", "Test User", UserAccountStatus.AVAILABLE, "imgUrl", Collections.emptyList());

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo("testUserId");
    }

    @Test
    @DisplayName("Delete Project - Valid Request")
    void testDeleteProject_ValidRequest() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId("testProjectId");
        when(projectDao.findById(anyString())).thenReturn(Optional.of(projectEntity));
        doNothing().when(projectDao).deleteById(anyString());

        DeleteProjectAdminUsecase.DeleteProjectAdminResponseDto response = adminService.deleteProject("testProjectId");

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("testProjectId");
    }

    @Test
    @DisplayName("Delete Project - Project Not Found")
    void testDeleteProject_ProjectNotFound() {
        when(projectDao.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteProject("invalidProjectId");
        });

        assertThat(exception.getMessage()).isEqualTo("Project not found.");
    }

    @Test
    @DisplayName("Delete User - Valid Request")
    void testDeleteUser_ValidRequest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testUserId");
        when(userDao.findById(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(userDao).delete(any(UserEntity.class));

        DeleteUserAdminUsecase.DeleteUserResponseDto response = adminService.deleteUser("testUserId");

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo("testUserId");
    }

    @Test
    @DisplayName("Delete User - User Not Found")
    void testDeleteUser_UserNotFound() {
        when(userDao.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteUser("invalidUserId");
        });

        assertThat(exception.getMessage()).isEqualTo("User not found.");
    }

    @Test
    @DisplayName("Get All Projects")
    void testGetAllProjects() {
        ProjectEntity projectEntity = new ProjectEntity();
        when(projectDao.findAll()).thenReturn(Collections.singletonList(projectEntity));

        GetAllProjectAdminUsecase.GetAllProjectResponseDto response = adminService.getAllProject();

        assertThat(response).isNotNull();
        assertThat(response.projectEntities()).hasSize(1);
    }

    @Test
    @DisplayName("Get All Users")
    void testGetAllUsers() {
        UserEntity userEntity = new UserEntity();
        when(userDao.findAll()).thenReturn(Collections.singletonList(userEntity));

        GetAllUserAdminUsecase.GetAllUserResponseDto response = adminService.getAllUser();

        assertThat(response).isNotNull();
        assertThat(response.userEntities()).hasSize(1);
    }

    @Test
    @DisplayName("Update Project - Valid Request")
    void testUpdateProject_ValidRequest() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId("testProjectId");
        when(projectDao.findById(anyString())).thenReturn(Optional.of(projectEntity));
        when(projectDao.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        UpdateProjectAdminUsecase.UpdateProjectAdminResponseDto response = adminService.updateProject("testProjectId", "Updated Project", "Updated Description",
                "imgUrl", Collections.emptyList(), 60, 3);

        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo("testProjectId");
    }

    @Test
    @DisplayName("Update Project - Project Not Found")
    void testUpdateProject_ProjectNotFound() {
        when(projectDao.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateProject("invalidProjectId", "Updated Project", "Updated Description",
                    "imgUrl", Collections.emptyList(), 60, 3);
        });

        assertThat(exception.getMessage()).isEqualTo("Project not found.");
    }

    @Test
    @DisplayName("Update User - Valid Request")
    void testUpdateUser_ValidRequest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testUserId");
        when(userDao.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userDao.save(any(UserEntity.class))).thenReturn(userEntity);

        UpdateUserAdminUsecase.UpdateUserResponseDto response = adminService.updateUser("testUserId", "test@example.com", "Updated User",
                UserAccountStatus.AVAILABLE, "imgUrl", Collections.emptyList());

        assertThat(response).isNotNull();
        assertThat(response.userEntity().getId()).isEqualTo("testUserId");
    }

    @Test
    @DisplayName("Update User - User Not Found")
    void testUpdateUser_UserNotFound() {
        when(userDao.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateUser("invalidUserId", "test@example.com", "Updated User",
                    UserAccountStatus.AVAILABLE, "imgUrl", Collections.emptyList());
        });

        assertThat(exception.getMessage()).isEqualTo("User not found.");
    }

    @Test
    @DisplayName("Search Users - Valid Request")
    void testSearchUsers_ValidRequest() {
        // Create a user entity with status
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testUserId");
        userEntity.setStatus(UserAccountStatus.AVAILABLE); // Set status to avoid NPE
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test User");

        when(userDao.findAll()).thenReturn(Collections.singletonList(userEntity));
        when(projectDao.findById(anyString())).thenReturn(Optional.of(new ProjectEntity()));

        // Perform search
        SearchUserAdminUsecase.SearchUserAdminResponseDto response = adminService.searchUsers("AVAILABLE", "email", "test@example.com");

        // Verify the result
        assertThat(response).isNotNull();
        assertThat(response.users()).hasSize(1);
        assertThat(response.users().get(0).user().getId()).isEqualTo("testUserId");
    }

    @Test
    @DisplayName("Search Projects - Valid Request")
    void testSearchProjects_ValidRequest() {
        // Create a user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testUserId");
        when(userDao.findById("testUserId")).thenReturn(Optional.of(userEntity));

        // Create a project entity
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId("testProjectId");
        projectEntity.setName("Test Project");
        projectEntity.setProgress(50);
        projectEntity.setLeftChanceForUserstory(5);
        projectEntity.setLastModifiedDate("2023-06-01T12:00:00.000000000");
        ProjectEntity.UserInProjectEntity userInProjectEntity = new ProjectEntity.UserInProjectEntity();
        userInProjectEntity.setUserId("testUserId");
        projectEntity.setUsers(Collections.singletonList(userInProjectEntity));

        when(projectDao.findAll()).thenReturn(Collections.singletonList(projectEntity));

        // Perform search
        SearchProjectAdminUsecase.SearchProjectAdminResponseDto response = adminService.searchProjects("Test Project", "testUserId", 5,
                "2023-01-01T00:00:00", "2023-12-31T23:59:59", 50, 1, 10);

        // Verify the result
        assertThat(response).isNotNull();
        assertThat(response.projects()).hasSize(1);
        assertThat(response.projects().get(0).getId()).isEqualTo("testProjectId");
    }


}