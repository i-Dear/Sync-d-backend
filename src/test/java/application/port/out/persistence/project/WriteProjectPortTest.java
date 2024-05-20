package application.port.out.persistence.project;

import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteProjectPortTest {
    private WriteProjectPort writeProjectPort = Mockito.mock(WriteProjectPort.class);
    private Project project;
    @BeforeEach
    void setUp(){
        String hostId = "hostUserId";
        project = new Project();
        project = project.createProjectDomain("Project Name", "Description", "img", hostId);
        project.setId("1");
    }
    @Test
    void createProjectShouldReturnNonNullId() {
        Mockito.when(writeProjectPort.CreateProject(project)).thenReturn("1");
        String projectId = writeProjectPort.CreateProject(project);
        assertNotNull(projectId, "CreateProject should return a non-null ID");
    }

    @Test
    void updateProjectShouldReturnUpdatedId() {
        Mockito.when(writeProjectPort.UpdateProject(project)).thenReturn("1");
        String updatedProjectId = writeProjectPort.UpdateProject(project);
        assertEquals("1", updatedProjectId, "UpdateProject should return the updated project ID");
    }

    @Test
    void removeProjectShouldNotThrowException() {
        assertDoesNotThrow(() -> writeProjectPort.RemoveProject("1"), "RemoveProject should not throw exception");
    }

    @Test
    void addProgressShouldReturnProjectId() {
        Mockito.when(writeProjectPort.AddProgress("1", 50)).thenReturn("1");
        String projectId = writeProjectPort.AddProgress("1", 50);
        assertEquals("1", projectId, "AddProgress should return the project ID");
    }

    @Test
    void updateLastModifiedDateShouldReturnProjectId() {
        Mockito.when(writeProjectPort.updateLastModifiedDate("1")).thenReturn("1");
        String projectId = writeProjectPort.updateLastModifiedDate("1");
        assertEquals("1", projectId, "updateLastModifiedDate should return the project ID");
    }
}
