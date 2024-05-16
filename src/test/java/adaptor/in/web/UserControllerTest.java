package adaptor.in.web;

import com.syncd.application.service.JwtService;
import com.syncd.adapter.in.web.UserController;
import com.syncd.application.port.in.GetUserInfoUsecase;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MockGetUserInfoUsecase implements GetUserInfoUsecase{

    @Override
    public GetUserInfoResponseDto getUserInfo(String userId) {
        return null;
    }
}

class UserControllerTest {

    private UserController userController;

    private GetUserInfoUsecase getUserInfoUsecase = new MockGetUserInfoUsecase();

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(getUserInfoUsecase, jwtService);
    }

    @Test
    void getUserInfo_ReturnsUserInfo_WhenValidTokenProvided() {
        // Arrange
        String token = "valid_token";
        String userId = "user123";
        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenReturn(userId);

        GetUserInfoUsecase.GetUserInfoResponseDto expectedUserInfo = new GetUserInfoUsecase.GetUserInfoResponseDto(null,null,null,null,null);
        // Assuming you have some mock data or create an instance here

        when(getUserInfoUsecase.getUserInfo(userId)).thenReturn(expectedUserInfo);

        // Act
        GetUserInfoUsecase.GetUserInfoResponseDto actualUserInfo = userController.getUserInfo(request);

        // Assert
        assertEquals(expectedUserInfo, actualUserInfo);
        verify(jwtService).resolveToken(request);
        verify(jwtService).getUserIdFromToken(token);
        verify(getUserInfoUsecase).getUserInfo(userId);
    }
}