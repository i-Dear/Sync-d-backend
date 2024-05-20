package mapper;

import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.enums.Role;
import com.syncd.mapper.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectMapperTest {
    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);
    private ProjectEntity entity1;
    private Project project1;
    private UserInProject user1;
    private UserInProject user2;

    @BeforeEach
    void setUp() {
        entity1 = createProjectEntity("id1", "Project 1");
        user1 = new UserInProject("user1@example.com", Role.MEMBER);
        user2 = new UserInProject("user2@example.com", Role.HOST);
        project1 = createProject("id1", "Project 1", "Description 1", 50, "2022-01-01", user1, user2);
    }

    @Test
    public void testMapProjectEntityToProject() {
        Project project = projectMapper.mapProjectEntityToProject(entity1);
        assertProjectFields(project, "id1", "Project 1");
    }

    @Test
    public void testMapProjectToProjectEntity() {
        ProjectEntity entity = projectMapper.mapProjectToProjectEntity(project1);
        assertProjectEntityFields(entity, "id1", "Project 1");
    }

    @Test
    public void testMapProjectEntitiesToProjects() {
        ProjectEntity entity2 = createProjectEntity("id2", "Project 2");
        List<ProjectEntity> entities = Arrays.asList(entity1, entity2);
        List<Project> projects = projectMapper.mapProjectEntitiesToProjects(entities);

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertProjectFields(projects.get(0), "id1", "Project 1");
        assertProjectFields(projects.get(1), "id2", "Project 2");
    }

    @Test
    public void testMapProjectsToProjectIds() {
        Project project2 = createProject("id2", "Project 2", null, 0, null);
        List<Project> projects = Arrays.asList(project1, project2);
        List<String> projectIds = projectMapper.mapProjectsToProjectIds(projects);

        assertNotNull(projectIds);
        assertEquals(2, projectIds.size());
        assertTrue(projectIds.contains("id1"));
        assertTrue(projectIds.contains("id2"));
    }

    @Test
    public void testConvertProjectToProjectForGetAllInfoDto() {
        ProjectForGetAllInfoAboutRoomsByUserIdResponseDto dto = projectMapper.convertProjectToProjectForGetAllInfoDto("user1@example.com", project1);
        assertProjectForGetAllInfoDtoFields(dto, "id1", "Project 1", "Description 1", Role.MEMBER, 50, "2022-01-01", "user1@example.com", "user2@example.com");
    }

    @Test
    public void testMapProjectsToGetAllRoomsByUserIdResponseDto() {
        Project project2 = createProject("id2", "Project 2", "Description 2", 70, "2022-02-01", user1);
        List<Project> projects = Arrays.asList(project1, project2);
        GetAllRoomsByUserIdResponseDto responseDto = projectMapper.mapProjectsToGetAllRoomsByUserIdResponseDto("user1@example.com", projects);

        assertNotNull(responseDto);
        assertEquals("user1@example.com", responseDto.userId());
        assertEquals(2, responseDto.projects().size());

        assertProjectForGetAllInfoDtoFields(responseDto.projects().get(0), "id1", "Project 1", "Description 1", Role.MEMBER, 50, "2022-01-01", "user1@example.com", "user2@example.com");
        assertProjectForGetAllInfoDtoFields(responseDto.projects().get(1), "id2", "Project 2", "Description 2", Role.MEMBER, 70, "2022-02-01", "user1@example.com");
    }

    private ProjectEntity createProjectEntity(String id, String name) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(id);
        entity.setName(name);
        return entity;
    }

    private Project createProject(String id, String name, String description, int progress, String lastModifiedDate, UserInProject... users) {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        project.setUsers(Arrays.asList(users));
        project.setProgress(progress);
        project.setLastModifiedDate(lastModifiedDate);
        return project;
    }

    private void assertProjectFields(Project project, String id, String name) {
        assertNotNull(project);
        assertEquals(id, project.getId());
        assertEquals(name, project.getName());
    }

    private void assertProjectEntityFields(ProjectEntity entity, String id, String name) {
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
    }

    private void assertProjectForGetAllInfoDtoFields(ProjectForGetAllInfoAboutRoomsByUserIdResponseDto dto, String id, String name, String description, Role role, int progress, String lastModifiedDate, String... userEmails) {
        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(description, dto.description());
        assertEquals(role, dto.role());
        assertEquals(progress, dto.progress());
        assertEquals(lastModifiedDate, dto.lastModifiedDate());
        assertEquals(userEmails.length, dto.userEmails().size());
        for (String email : userEmails) {
            assertTrue(dto.userEmails().contains(email));
        }
    }
}
