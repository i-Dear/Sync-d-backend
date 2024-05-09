package application.port.out.persistence.project;

import com.syncd.adapter.out.persistence.ProjectPersistenceAdapter;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.domain.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReadProjectPortTest {
    @Mock
    private ProjectDao projectDao;

    @InjectMocks
    private ProjectPersistenceAdapter projectPersistenceAdapter;
    private ProjectEntity projectEntity1;
    private ProjectEntity projectEntity2;
    @BeforeEach
    void setUp() {
        projectEntity1 = new ProjectEntity();
        projectEntity1.setId("1");
        projectEntity1.setName("Project One");
        projectEntity1.setDescription("Description of Project One");
        projectEntity1.setImg("image1.jpg");

        projectEntity2 = new ProjectEntity();
        projectEntity2.setId("2");
        projectEntity2.setName("Project Two");
        projectEntity2.setDescription("Description of Project Two");
        projectEntity2.setImg("image2.jpg");
    }

    @Test
    void testFindAllProjectsByUserId() {
        List<Project> projects = projectPersistenceAdapter.findAllProjectByUserId("user1");
        when(projectDao.findById("1")).thenReturn(Optional.of(projectEntity1));

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals(2, projects.size());
        assertEquals("Project One", projects.get(0).getName());
        assertEquals("Project Two", projects.get(1).getName());
        lenient().when(projectDao.findByUsersUserId("user1")).thenReturn(Arrays.asList(projectEntity1, projectEntity2));
    }

    @Test
    void testFindProjectByProjectId() {
        when(projectDao.findById("1")).thenReturn(Optional.of(projectEntity1));

        Project project = projectPersistenceAdapter.findProjectByProjectId("1");

        assertNotNull(project);
        assertEquals("Project One", project.getName());
        assertEquals("Description of Project One", project.getDescription());
    }
}
