package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.in.UpdateProjectUsecase;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.exceptions.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateProjectUsecaseTest {
    @Mock
    private UpdateProjectUsecase updateProjectUsecase;

    @Test
    @DisplayName("ProjectID를 기반으로 Project 정보 업데이트 테스트")
    void testUpdateProject(){
        String userId = "user123";
        String projectId = "project456";
        String projectName = "공감대";
        String description = "이해하지마 공감해";
        String image = "updated_image.jpg";

        UpdateProjectResponseDto expectedResponse = new UpdateProjectResponseDto(projectId);
        when(updateProjectUsecase.updateProject(userId, projectId, projectName, description, image))
                .thenReturn(expectedResponse);

        UpdateProjectResponseDto actualResponse = updateProjectUsecase.updateProject(userId, projectId, projectName, description, image);

        assertEquals(expectedResponse.projectId(), actualResponse.projectId());
        verify(updateProjectUsecase).updateProject(userId, projectId, projectName, description, image);
    }
    @Test
    @DisplayName("Project 업데이트 시 존재하지 않는 프로젝트에 대한 예외 처리")
    void testUpdateNonExistingProject() {
        String userId = "user123";
        String projectId = "nonExistingProject";
        String projectName = "공감대";
        String description = "이해하지마 공감해";
        String image = "updated_image.jpg";

        when(updateProjectUsecase.updateProject(userId, projectId, projectName, description, image))
                .thenThrow(new ProjectNotFoundException("Project not found with ID: " + projectId));

        assertThrows(ProjectNotFoundException.class, () -> updateProjectUsecase.updateProject(userId, projectId, projectName, description, image),
                "Updating a non-existing project should throw ProjectNotFoundException.");
    }
}
