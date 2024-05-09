package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.in.DeleteProjectUsecase;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteProjectUsecaseTest {
    @Mock
    private DeleteProjectUsecase deleteProjectUsecase;

    @Test
    @DisplayName("")
    void testDeleteProject(){
        String userId = "user1";
        String projectId = "project123";
        DeleteProjectResponseDto expectedResponse = new DeleteProjectResponseDto(projectId);

        when(deleteProjectUsecase.deleteProject(userId, projectId)).thenReturn(expectedResponse);
        DeleteProjectResponseDto actualResponse = deleteProjectUsecase.deleteProject(userId, projectId);
        assertEquals(expectedResponse.projectId(), actualResponse.projectId(), "ProjectID와 삭제된 ProjectID가 불일치합니다.");
    }
}
