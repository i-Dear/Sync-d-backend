package adaptor.in.web;

import com.syncd.AuthControllerProperties;
import com.syncd.adapter.in.web.AuthController;
import com.syncd.application.port.in.SocialLoginUsecase;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.view.RedirectView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private SocialLoginUsecase socialLoginUsecase;

    @Mock
    private AuthControllerProperties authControllerProperties;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Google Login - Valid Request")
    void testGoogleLogin_ValidRequest() {
        String code = "testCode";
        String registrationId = "google";
        String redirectUrl = "http://localhost:8080/redirect";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(authControllerProperties.getRedirectUrl()).thenReturn(redirectUrl);
        when(socialLoginUsecase.socialLogin(anyString(), anyString())).thenReturn(new TokenDto(accessToken, refreshToken));

        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectView result = authController.googleLogin(code, registrationId, response);

        assertThat(result.getUrl()).isEqualTo(redirectUrl + accessToken);

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> registrationIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(socialLoginUsecase, times(1)).socialLogin(codeCaptor.capture(), registrationIdCaptor.capture());

        assertThat(codeCaptor.getValue()).isEqualTo(code);
        assertThat(registrationIdCaptor.getValue()).isEqualTo(registrationId);
    }
}
