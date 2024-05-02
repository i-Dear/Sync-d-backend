package application.port.in;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetRoomAuthTokenUsecaseTest {
    @Mock
    private GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;

    @Test
    @DisplayName("UserID를 기반으로한 토큰 발급 테스트")
    void testGetRoomAuthToken(){
        String userId = "123";
        GetRoomAuthTokenResponseDto expectedResponse = new GetRoomAuthTokenResponseDto("validToken123");
        when(getRoomAuthTokenUsecase.getRoomAuthToken(userId)).thenReturn(expectedResponse);

        GetRoomAuthTokenResponseDto actualResponse = getRoomAuthTokenUsecase.getRoomAuthToken(userId);

        assertEquals(expectedResponse.token(), actualResponse.token());
        verify(getRoomAuthTokenUsecase).getRoomAuthToken(userId);
    }
}
