package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.syncd.application.port.in.InviteUserInProjectUsecase;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;

import com.syncd.exceptions.CustomException;
import com.syncd.exceptions.ErrorInfo;
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
    String userId = "user123";
    List<String> users = List.of("user234", "user345");
    @Test
    void testInviteUserInProject(){
        String projectId = "project456";
        InviteUserInProjectResponseDto expectedResponse = new InviteUserInProjectResponseDto(projectId);
        when(inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users)).thenReturn(expectedResponse);

        InviteUserInProjectResponseDto actualResponse = inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users);
        assertEquals(expectedResponse.projectId(), actualResponse.projectId());
        verify(inviteUserInProjectUsecase).inviteUserInProject(userId, projectId, users);
    }
    @Test
    @DisplayName("Project 초대 시 존재하지 않는 Project에 대한 예외 처리")
    void testInviteUserInNonExistingProject() {
        String projectId = "nonExistingProject";

        when(inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users))
                .thenThrow(new CustomException(ErrorInfo.PROJECT_NOT_FOUND, "project id : " + projectId));

        assertThrows(CustomException.class, () -> inviteUserInProjectUsecase.inviteUserInProject(userId, projectId, users),
                "Should throw ProjectNotFoundException if the project does not exist");
    }
}
