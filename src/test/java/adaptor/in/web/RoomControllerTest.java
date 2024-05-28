package adaptor.in.web;

import com.syncd.adapter.in.web.RoomController;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RoomControllerTest {

    @Mock
    private GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;

    @Mock
    private GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======================================
    // GetRoomAuthTokenUsecase
    // ======================================

    @Test
    @DisplayName("Get Room Auth Token - Valid Request")
    void testGetRoomAuthToken_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setupMockJwtService(request, "token", "userId");

        GetRoomAuthTokenResponseDto responseDto = new GetRoomAuthTokenResponseDto("authToken");
        when(getRoomAuthTokenUsecase.getRoomAuthToken(anyString())).thenReturn(responseDto);

        GetRoomAuthTokenResponseDto response = roomController.getRoomAuthToken(request);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("authToken");

        verifyJwtServiceInteraction(request, "token");
        verifyGetRoomAuthToken("userId");
    }

    @Test
    @DisplayName("Get Room Auth Token - Test - Valid Request")
    void testGetRoomAuthTokenTest_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setupMockJwtService(request, "token", "userId");

        TestDto testDto = new TestDto("validRoomId");
        GetRoomAuthTokenResponseDto responseDto = new GetRoomAuthTokenResponseDto("testAuthToken");
        when(getRoomAuthTokenUsecase.Test(anyString(), anyString())).thenReturn(responseDto);

        GetRoomAuthTokenResponseDto response = roomController.getRoomAuthToken(testDto, request);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("testAuthToken");

        verifyJwtServiceInteraction(request, "token");
        verifyTestRoomAuthToken("userId", "validRoomId");
    }

    // ======================================
    // GetAllRoomsByUserIdUsecase
    // ======================================

    @Test
    @DisplayName("Get All Rooms By User Id - Valid Request")
    void testGetAllRoomsByUserId_ValidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setupMockJwtService(request, "token", "userId");

        GetAllRoomsByUserIdResponseDto responseDto = new GetAllRoomsByUserIdResponseDto("userId", Collections.emptyList());
        when(getAllRoomsByUserIdUsecase.getAllRoomsByUserId(anyString())).thenReturn(responseDto);

        GetAllRoomsByUserIdResponseDto response = roomController.getAllInfoAboutRoomsByUserId(request);

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo("userId");

        verifyJwtServiceInteraction(request, "token");
        verifyGetAllRoomsByUserId("userId");
    }
    // Helper methods for mock setup
    private void setupMockJwtService(HttpServletRequest request, String token, String userId) {
        when(jwtService.resolveToken(request)).thenReturn(token);
        when(jwtService.getUserIdFromToken(token)).thenReturn(userId);
    }

    private void verifyJwtServiceInteraction(HttpServletRequest request, String token) {
        verify(jwtService).resolveToken(request);
        verify(jwtService).getUserIdFromToken(token);
    }

    private void verifyGetRoomAuthToken(String userId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(getRoomAuthTokenUsecase).getRoomAuthToken(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    private void verifyGetAllRoomsByUserId(String userId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(getAllRoomsByUserIdUsecase).getAllRoomsByUserId(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    private void verifyTestRoomAuthToken(String userId, String roomId) {
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> roomIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(getRoomAuthTokenUsecase).Test(userIdCaptor.capture(), roomIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(roomIdCaptor.getValue()).isEqualTo(roomId);
    }

}
