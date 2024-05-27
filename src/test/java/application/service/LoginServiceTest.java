package application.service;

import com.syncd.application.port.in.GenerateTokenUsecase;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.service.LoginService;
import com.syncd.dto.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WriteUserPort writeUserPort;

    @Mock
    private GenerateTokenUsecase generateTokenUsecase;

    @Mock
    private ReadUserPort readUserPort;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSocialLoginWithGoogleRegistrationId() {
        // Given
        String authorizationCode = "sample_authorization_code";
        String registrationId = "google";

        // Mocking the behavior of restTemplate.exchange(...) method
        // Here you should mock the behavior to return a proper response entity
        // For simplicity, let's assume it returns a valid response node
        // when calling with the given parameters
        // For actual implementation, you should define the behavior according to your needs
        // For example:
        // when(restTemplate.exchange(anyString(), any(), any(), eq(JsonNode.class)))
        //         .thenReturn(new ResponseEntity<>(mockResponseNode, HttpStatus.OK));

        // When
        TokenDto tokenDto = loginService.socialLogin(authorizationCode, registrationId);

        // Then
        // Assert that the returned tokenDto is not null
        // Add more assertions based on your actual implementation
        // For example, you can assert that the generated access token is not empty
        assertEquals("google", registrationId);
        // Add more assertions based on your actual implementation
    }
}
