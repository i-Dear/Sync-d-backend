package adaptor.in.web;

import com.syncd.adapter.in.web.UserController;
import com.syncd.application.port.in.GetUserInfoUsecase;
import com.syncd.application.port.in.GetUserInfoUsecase.GetUserInfoResponseDto;
import com.syncd.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private GetUserInfoUsecase getUserInfoUsecase;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get User Info - Valid Request")
    void testGetUserInfo_ValidRequest() {
        String token = "test-token";
        String userId = "user-id";

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenReturn(userId);

        GetUserInfoResponseDto userInfoResponseDto = new GetUserInfoResponseDto(
                "user-id",
                "Test User",
                "profileImg.jpg",
                "test@example.com",
                Collections.emptyList()
        );
        when(getUserInfoUsecase.getUserInfo(userId)).thenReturn(userInfoResponseDto);

        GetUserInfoResponseDto response = userController.getUserInfo(request);

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo("user-id");
        assertThat(response.name()).isEqualTo("Test User");
        assertThat(response.img()).isEqualTo("profileImg.jpg");
        assertThat(response.email()).isEqualTo("test@example.com");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(getUserInfoUsecase, times(1)).getUserInfo(captor.capture());
        assertThat(captor.getValue()).isEqualTo(userId);

        verify(jwtService, times(1)).resolveToken(request);
        verify(jwtService, times(1)).getUserIdFromToken(token);
    }

    @Test
    @DisplayName("Get User Info - Invalid Token")
    void testGetUserInfo_InvalidToken() {
        String token = "invalid-token";

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenThrow(new IllegalArgumentException("Invalid token"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.getUserInfo(request);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid token");

        verify(jwtService, times(1)).resolveToken(request);
        verify(jwtService, times(1)).getUserIdFromToken(token);
        verify(getUserInfoUsecase, never()).getUserInfo(anyString());
    }
}