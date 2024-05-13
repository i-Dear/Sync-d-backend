package com.syncd.application.port.out.persistence.project;

import com.syncd.domain.project.Project;
import com.syncd.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadProjectPortTest {
    private ReadProjectPort readProjectPort = Mockito.mock(ReadProjectPort.class);

    private Project project;
    @BeforeEach
    void setUp(){
        String hostId = "hostUserId";
        List<User> emptyUserList = new ArrayList<>();
        project = new Project();
        project = project.createProjectDomain("Project Name", "Description", "img", hostId, emptyUserList);
        project.setId("1");
        project.setLastModifiedDate(LocalDateTime.now().toString());
        project.setProgress(0);
    }

    @Test
    void findAllProjectsByUserIdShouldReturnNonEmptyList() {
        when(readProjectPort.findAllProjectByUserId(anyString())).thenReturn(Collections.singletonList(project));

        List<Project> projects = readProjectPort.findAllProjectByUserId("user123");
        assertFalse(projects.isEmpty(), "findAllProjectByUserId should return a non-empty list");
        assertEquals("1", projects.get(0).getId(), "The returned project should have the correct ID");
    }

    @Test
    void findAllProjectsByUserIdShouldHandleNoProjects() {
        when(readProjectPort.findAllProjectByUserId(anyString())).thenReturn(Collections.emptyList());

        List<Project> projects = readProjectPort.findAllProjectByUserId("user123");
        assertTrue(projects.isEmpty(), "findAllProjectByUserId should handle cases with no projects");
    }

    @Test
    void findProjectByProjectIdShouldReturnCorrectProject() {
        when(readProjectPort.findProjectByProjectId(anyString())).thenReturn(project);

        Project foundProject = readProjectPort.findProjectByProjectId("1");
        assertNotNull(foundProject, "findProjectByProjectId should return a non-null project");
        assertEquals("1", foundProject.getId(), "The project should have the correct ID");
    }

    @Test
    void findProjectByProjectIdShouldHandleNotFound() {
        when(readProjectPort.findProjectByProjectId(anyString())).thenReturn(null);

        Project project = readProjectPort.findProjectByProjectId("nonexistent");
        assertNull(project, "findProjectByProjectId should handle not found projects correctly");
    }
}