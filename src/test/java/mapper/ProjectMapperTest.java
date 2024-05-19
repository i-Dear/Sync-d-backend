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
        entity1 = new ProjectEntity();
        entity1.setId("id1");
        entity1.setName("Project 1");

        user1 = new UserInProject("user1@example.com", Role.MEMBER);
        user2 = new UserInProject("user2@example.com", Role.HOST);

        project1 = new Project();
        project1.setId("id1");
        project1.setName("Project 1");
        project1.setDescription("Description 1");
        project1.setUsers(Arrays.asList(user1, user2));
        project1.setProgress(50);
        project1.setLastModifiedDate("2022-01-01");
    }

    @Test
    public void testMapProjectEntitiesToProjects() {
        ProjectEntity entity2 = new ProjectEntity();
        entity2.setId("id2");
        entity2.setName("Project 2");

        List<ProjectEntity> entities = Arrays.asList(entity1, entity2);

        List<Project> projects = projectMapper.mapProjectEntitiesToProjects(entities);

        assertNotNull(projects);
        assertEquals(2, projects.size());

        assertEquals("id1", projects.get(0).getId());
        assertEquals("Project 1", projects.get(0).getName());
        assertEquals("id2", projects.get(1).getId());
        assertEquals("Project 2", projects.get(1).getName());
    }

    @Test
    public void testMapProjectsToProjectIds() {
        Project project2 = new Project();
        project2.setId("id2");
        project2.setName("Project 2");

        List<Project> projects = Arrays.asList(project1, project2);

        List<String> projectIds = projectMapper.mapProjectsToProjectIds(projects);

        assertNotNull(projectIds);
        assertEquals(2, projectIds.size());
        assertTrue(projectIds.contains("id1"));
        assertTrue(projectIds.contains("id2"));
    }

    @Test
    public void testFromProjectEntity() {
        Project project = projectMapper.mapProjectEntityToProject(entity1);

        assertNotNull(project);
        assertEquals("id1", project.getId());
        assertEquals("Project 1", project.getName());
    }

    @Test
    public void testToProjectEntity() {
        ProjectEntity entity = projectMapper.mapProjectToProjectEntity(project1);

        assertNotNull(entity);
        assertEquals("id1", entity.getId());
        assertEquals("Project 1", entity.getName());
    }

    @Test
    public void testConvertProjectToDto() {
        ProjectForGetAllInfoAboutRoomsByUserIdResponseDto dto = projectMapper.convertProjectToProjectForGetAllInfoDto("user1@example.com", project1);

        assertNotNull(dto);
        assertEquals("id1", dto.id());
        assertEquals("Project 1", dto.name());
        assertEquals("Description 1", dto.description());
        assertEquals(Role.MEMBER, dto.role());
        assertEquals(2, dto.userEmails().size());
        assertTrue(dto.userEmails().contains("user1@example.com"));
        assertTrue(dto.userEmails().contains("user2@example.com"));
        assertEquals(50, dto.progress());
        assertEquals("2022-01-01", dto.lastModifiedDate());
    }

    @Test
    public void testMapProjectsToResponse() {
        Project project2 = new Project();
        project2.setId("id2");
        project2.setName("Project 2");
        project2.setDescription("Description 2");
        project2.setUsers(Arrays.asList(user1));
        project2.setProgress(70);
        project2.setLastModifiedDate("2022-02-01");

        List<Project> projects = Arrays.asList(project1, project2);

        GetAllRoomsByUserIdResponseDto responseDto = projectMapper.mapProjectsToGetAllRoomsByUserIdResponseDto("user1@example.com", projects);

        assertNotNull(responseDto);
        assertEquals("user1@example.com", responseDto.userId());
        assertEquals(2, responseDto.projects().size());

        ProjectForGetAllInfoAboutRoomsByUserIdResponseDto dto1 = responseDto.projects().get(0);
        assertEquals("id1", dto1.id());
        assertEquals("Project 1", dto1.name());
        assertEquals("Description 1", dto1.description());
        assertEquals(Role.MEMBER, dto1.role());
        assertEquals(2, dto1.userEmails().size());
        assertTrue(dto1.userEmails().contains("user1@example.com"));
        assertTrue(dto1.userEmails().contains("user2@example.com"));
        assertEquals(50, dto1.progress());
        assertEquals("2022-01-01", dto1.lastModifiedDate());

        ProjectForGetAllInfoAboutRoomsByUserIdResponseDto dto2 = responseDto.projects().get(1);
        assertEquals("id2", dto2.id());
        assertEquals("Project 2", dto2.name());
        assertEquals("Description 2", dto2.description());
        assertEquals(Role.MEMBER, dto2.role()); // 기본 역할로 설정됨
        assertEquals(1, dto2.userEmails().size());
        assertTrue(dto2.userEmails().contains("user1@example.com"));
        assertEquals(70, dto2.progress());
        assertEquals("2022-02-01", dto2.lastModifiedDate());
    }
}
