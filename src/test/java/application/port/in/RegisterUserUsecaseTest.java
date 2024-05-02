package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.in.RegitsterUserUsecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUsecaseTest {
    @Mock
    private RegitsterUserUsecase registerUserUsecase;

    @Test
    @DisplayName("로그인 후 access, refresh token 발급 테스트")
    void testUserRegistration(){
        RegisterUserRequestDto registerDto = new RegisterUserRequestDto("민수", "민수@example.com", "password123");
        RegisterUserResponseDto expectedResponse = new RegisterUserResponseDto("access_token123", "refresh_token456");

        when(registerUserUsecase.registerUser(registerDto)).thenReturn(expectedResponse);

        RegisterUserResponseDto actualResponse = registerUserUsecase.registerUser(registerDto);

        assertEquals(expectedResponse.accessToken(), actualResponse.accessToken(), "Access Token이 매칭이 안됩니다");
        assertEquals(expectedResponse.refreshToken(), actualResponse.refreshToken(), "Refresh Token이 매칭이 안됩니다.");
        verify(registerUserUsecase).registerUser(registerDto);
    }

    @Test
    @DisplayName("")
    void testRegistrationWithInvailData(){
        RegisterUserRequestDto registerDto = new RegisterUserRequestDto("", "", "");  // Invalid data

        when(registerUserUsecase.registerUser(registerDto))
                .thenThrow(new IllegalArgumentException("Invalid registration data"));

        assertThrows(IllegalArgumentException.class, () -> registerUserUsecase.registerUser(registerDto),
                "Data가 맞지 않을 때 exception이 생겨야합니다.");
    }
}
