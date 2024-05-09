package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetAllRoomsByUserIdUsecaseTest {
    @Mock
    private GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;

    @Test
    @DisplayName("")
    void testGetAllRoomByUserId(){
        String userId = "user1";
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projects = List.of(
                new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto("Project A", "1", "Description A", Role.HOST),
                new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto("Project B", "2", "Description B", Role.MEMBER)
        );

        GetAllRoomsByUserIdResponseDto expectedResponse = new GetAllRoomsByUserIdResponseDto(userId, projects);

        when(getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId)).thenReturn(expectedResponse);

        GetAllRoomsByUserIdResponseDto actualResponse = getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId);

        assertEquals(expectedResponse.userId(), actualResponse.userId(), "User ID가 다릅니다.");
        assertIterableEquals(expectedResponse.projects(), actualResponse.projects(), "Project 이름이 다릅니다.");

    }
}
