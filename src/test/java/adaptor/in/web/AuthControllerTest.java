package adaptor.in.web;

import com.syncd.AuthControllerProperties;
import com.syncd.adapter.in.web.AuthController;
import com.syncd.application.port.in.SocialLoginUsecase;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.servlet.view.RedirectView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private SocialLoginUsecase socialLoginUsecase;

    @Mock
    private AuthControllerProperties authControllerProperties;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        socialLoginUsecase = Mockito.mock(SocialLoginUsecase.class);
        authControllerProperties = Mockito.mock(AuthControllerProperties.class);

        authController = new AuthController(socialLoginUsecase,authControllerProperties);
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
        when(socialLoginUsecase.socialLogin(anyString(), anyString(),any())).thenReturn(new TokenDto(accessToken, refreshToken));

        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectView result = authController.googleLogin(code, registrationId, response);

        assertThat(result.getUrl()).isEqualTo(redirectUrl + accessToken);

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redirectionUriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> registrationIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(socialLoginUsecase, times(1)).socialLogin(codeCaptor.capture(), registrationIdCaptor.capture(),redirectionUriCaptor.capture());

        assertThat(codeCaptor.getValue()).isEqualTo(code);
        assertThat(registrationIdCaptor.getValue()).isEqualTo(registrationId);
    }
}
