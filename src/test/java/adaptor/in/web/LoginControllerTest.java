package adaptor.in.web;

import com.syncd.GoogleOAuth2Properties;
import com.syncd.adapter.in.web.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.view.RedirectView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private GoogleOAuth2Properties googleOAuth2Properties;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Redirect to Google OAuth - Valid Request")
    void testRedirectToGoogleOAuth_ValidRequest() {
        String redirectUri = "http://localhost:8080/redirect";
        when(googleOAuth2Properties.getRedirectUri()).thenReturn(redirectUri);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("http://localhost:8080/");

        RedirectView result = loginController.redirectToGoogleOAuth(request);

        String expectedUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=70988875044-9nmbvd2suleub4ja095mrh83qbi7140j.apps.googleusercontent.com" +
                "&redirect_uri=http://localhost:8080/login/oauth2/code/google" +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

        assertThat(result.getUrl()).isEqualTo(expectedUrl);
    }

//    @Test
//    @DisplayName("Test Redirect to Google OAuth - No Referer Header")
//    void testRedirectToGoogleOAuth_NoRefererHeader() {
//        String redirectUri = "http://localhost:8080/redirect";
//        when(googleOAuth2Properties.getRedirectUri()).thenReturn(redirectUri);
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Referer")).thenReturn(null);
//
//        RedirectView result = loginController.redirectToGoogleOAuth(request);
//
//        String expectedUrl = "https://accounts.google.com/o/oauth2/auth" +
//                "?client_id=70988875044-9nmbvd2suleub4ja095mrh83qbi7140j.apps.googleusercontent.com" +
//                "&redirect_uri=http://localhost:8080/redirect" +
//                "&response_type=code" +
//                "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
//
//        assertThat(result.getUrl()).isEqualTo(expectedUrl);
//    }
}
