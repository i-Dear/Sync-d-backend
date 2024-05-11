package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.syncd.application.port.in.InviteUserInProjectUsecase;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;

import com.syncd.exceptions.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InviteUserInProjectUsecaseTest {
    @Mock
    private InviteUserInProjectUsecase inviteUserInProjectUsecase;

    @Test
    void testInviteUserInProject(){
        String userId = "user123";
        String projectId = "project456";
        List<String> users = List.of("user234","user345");
        InviteUserInProjectResponseDto expectedResponse = new InviteUserInProjectResponseDto(projectId);
        when(inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users)).thenReturn(expectedResponse);

        InviteUserInProjectResponseDto actualResponse = inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users);
        assertEquals(expectedResponse.projectId(), actualResponse.projectId());
        verify(inviteUserInProjectUsecase).inviteUserInProject(userId, projectId, users);
    }
    @Test
    @DisplayName("Project 초대 시 존재하지 않는 Project에 대한 예외 처리")
    void testInviteUserInNonExistingProject() {
        String userId = "user123";
        String projectId = "nonExistingProject";
        List<String> users = List.of("user234", "user345");

        when(inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        assertThrows(ProjectNotFoundException.class, () -> inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users),
                "Should throw ProjectNotFoundException if the project does not exist");
    }
}
