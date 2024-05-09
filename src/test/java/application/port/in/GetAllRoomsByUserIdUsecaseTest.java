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
    @DisplayName("Test fetching all rooms by user ID")
    void testGetAllRoomByUserId() {
        String userId = "user1";
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projects = List.of(
                new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto("Project A", "1", "Description A", Role.HOST, List.of("userA@example.com"), 90, 20200101),
                new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto("Project B", "2", "Description B", Role.MEMBER, List.of("userB@example.com"), 75, 20200201)
        );

        GetAllRoomsByUserIdResponseDto expectedResponse = new GetAllRoomsByUserIdResponseDto(userId, projects);

        when(getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId)).thenReturn(expectedResponse);

        GetAllRoomsByUserIdResponseDto actualResponse = getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId);

        assertEquals(expectedResponse.userId(), actualResponse.userId(), "User ID does not match.");
        assertIterableEquals(expectedResponse.projects(), actualResponse.projects(), "Projects do not match.");
    }
}
