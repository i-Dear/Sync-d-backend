package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.syncd.application.port.in.WithdrawUserInProjectUsecase;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class WithdrawUserInProjectUsecaseTest {
    @Mock
    private WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;

    @Test
    @DisplayName("ProjectID를 기반으로 Project 삭제 테스트")
    void testWithdrawUserInProject(){
        String userId = "user123";
        String projectId = "project456";
        List<String> users = List.of("user234", "user345");
        WithdrawUserInProjectResponseDto expectedResponse = new WithdrawUserInProjectResponseDto(projectId);

        when(withdrawUserInProjectUsecase.withdrawUserInProject(userId, projectId, users))
                .thenReturn(expectedResponse);

        WithdrawUserInProjectResponseDto actualResponse = withdrawUserInProjectUsecase.withdrawUserInProject(userId, projectId, users);

        assertEquals(expectedResponse.projectId(), actualResponse.projectId());
        verify(withdrawUserInProjectUsecase).withdrawUserInProject(userId, projectId, users);
    }
}
