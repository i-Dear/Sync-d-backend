package adaptor.out.persistence;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.adapter.out.persistence.ProjectPersistenceAdapter;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.domain.project.Project;
import com.syncd.domain.user.User;
import com.syncd.exceptions.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectPersistenceAdapterTest {
    @Mock
    private ProjectDao projectDao;

    @InjectMocks
    private ProjectPersistenceAdapter adapter;

    private Project project;
    private ProjectEntity projectEntity;

    @BeforeEach
    void setup() {
        projectEntity = new ProjectEntity();
        projectEntity.setId("1");
        String hostId = "hostId";
        List<User> userList = new ArrayList<>();
        project = new Project();
        project = project.createProjectDomain("Project Name", "Description", "img", hostId);
        project.setId("1");
    }

    @Test
    void testFindAllProjectByUserId() {
        when(projectDao.findByUsersUserId("user1")).thenReturn(Arrays.asList(projectEntity));

        List<Project> projects = adapter.findAllProjectByUserId("user1");

        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals("1", projects.get(0).getId());
    }

    @Test
    void testFindProjectByProjectId() {
        when(projectDao.findById("1")).thenReturn(Optional.of(projectEntity));

        Project foundProject = adapter.findProjectByProjectId("1");

        assertNotNull(foundProject);
        assertEquals("1", foundProject.getId());
    }

    @Test
    void testFindProjectByProjectId_NotFound() {
        when(projectDao.findById("1")).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> adapter.findProjectByProjectId("1"));
    }

    @Test
    void testCreateProject() {
        when(projectDao.existsById("1")).thenReturn(false);
        when(projectDao.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        String projectId = adapter.CreateProject(project);

        assertNotNull(projectId);
        assertEquals("1", projectId);
    }

    @Test
    void testCreateProject_AlreadyExists() {
        when(projectDao.existsById("1")).thenReturn(true);

        assertThrows(CustomException.class, () -> adapter.CreateProject(project));
    }

    @Test
    void testRemoveProject() {
        when(projectDao.existsById("1")).thenReturn(true);
        doNothing().when(projectDao).deleteById("1");

        assertDoesNotThrow(() -> adapter.RemoveProject("1"));
        verify(projectDao).deleteById("1");
    }

    @Test
    void testRemoveProject_NotFound() {
        when(projectDao.existsById("1")).thenReturn(false);

        assertThrows(CustomException.class, () -> adapter.RemoveProject("1"));
    }

    @Test
    void testUpdateProject() {
        when(projectDao.existsById("1")).thenReturn(true);
        when(projectDao.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        String updatedId = adapter.UpdateProject(project);

        assertNotNull(updatedId);
        assertEquals("1", updatedId);
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectDao.existsById("1")).thenReturn(false);

        assertThrows(CustomException.class, () -> adapter.UpdateProject(project));
    }
}
