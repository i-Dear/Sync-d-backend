package application.port.out.liveblock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.dto.LiveblocksTokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LiveblockPortTest {
    @Mock
    private LiveblocksPort liveblocksPort;

    @Test
    @DisplayName("")
    void testGetRoomAuthToken(){
        String userId = "user123";
        String name = "태욱";
        String img = "profile.jpg";
        List<String> projectIds = List.of("project1", "project2");
        LiveblocksTokenDto expectedToken = new LiveblocksTokenDto("access_token123");

        when(liveblocksPort.GetRoomAuthToken(userId, name, img, projectIds)).thenReturn(expectedToken);

        LiveblocksTokenDto actualToken = liveblocksPort.GetRoomAuthToken(userId, name, img, projectIds);

        assertNotNull(actualToken);
        assertEquals(expectedToken.token(), actualToken.token());
    }
}
