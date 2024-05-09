package application.port.out.autentication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.dto.TokenDto;
import com.syncd.dto.UserForTokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AutenticationPortTest {
    @Mock
    private AuthenticationPort authenticationPort;

    @Test
    @DisplayName("")
    void testGetJwtTokens(){
        UserForTokenDto userForTokenDto = new UserForTokenDto("user123");

        TokenDto expectedToken = new TokenDto("accessToken123", "refreshToken123");

        when(authenticationPort.GetJwtTokens(userForTokenDto)).thenReturn(expectedToken);

        TokenDto actualToken = authenticationPort.GetJwtTokens(userForTokenDto);

        assertNotNull(actualToken, "Token이 Null입니다.");
        assertEquals(expectedToken.accessToken(), actualToken.accessToken(), "Access Token이 매칭되지 않습니다.");
        assertEquals(expectedToken.refreshToken(), actualToken.refreshToken(), "Refresh Token이 매칭되지 않습니다.");
        verify(authenticationPort).GetJwtTokens(userForTokenDto);
    }
}
