package application.service;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.user.User;
import com.syncd.application.service.UserService;
import com.syncd.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private ReadUserPort readUserPort;

    @Mock
    private GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserInfo_UserNotFound() {
        // Given
        String userId = "user123";

        // When
        when(readUserPort.findByUserId(userId)).thenReturn(null);

        // Then
        assertThrows(RuntimeException.class, () -> {
            userService.getUserInfo(userId);
        });
    }

    @Test
    public void testGetUserInfo() {
        // Given
        String userId = "user123";
        User user = new User();
        user.setId(userId);
        user.setName("syncd");
        user.setEmail("syncd.doe@example.com");
        user.setProfileImg("profile.jpg");

        GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto project1 =
                new GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                        "project1", "1", "description1", Role.HOST, List.of("user1@example.com"), 0, "2024-05-19","img");
        GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto project2 =
                new GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                        "project2", "2", "description2", Role.MEMBER, List.of("user2@example.com"), 50, "2024-05-20","img");
        GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto projects =
                new GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto(userId, List.of(project1, project2));

        // When
        when(readUserPort.findByUserId(userId)).thenReturn(user);
        when(getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId)).thenReturn(projects);

        // Act
        UserService.GetUserInfoResponseDto response = userService.getUserInfo(userId);

        // Then
        assertEquals(userId, response.userId());
        assertEquals("syncd", response.name());
        assertEquals("syncd.doe@example.com", response.email());
        assertEquals("profile.jpg", response.img());
        assertEquals(2, response.projects().size());
        assertEquals("project1", response.projects().get(0).name());
        assertEquals("project2", response.projects().get(1).name());
    }
}
