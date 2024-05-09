package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.syncd.application.port.in.InviteUserInProjectUsecase;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;

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
    @DisplayName("")
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
}
