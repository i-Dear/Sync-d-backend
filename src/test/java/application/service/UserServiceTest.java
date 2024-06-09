package application.service;

import Dummy.Stub.application.out.gmail.StubSendMailPort;
import Dummy.Stub.application.out.liveblock.StubLiveblocksPort;
import Dummy.Stub.application.out.openai.StubChatGPTPort;
import Dummy.Stub.application.out.persistence.project.StubReadProjectPort;
import Dummy.Stub.application.out.persistence.project.StubWriteProjectPort;
import Dummy.Stub.application.out.persistence.user.StubReadUserPort;
import Dummy.Stub.application.out.persistence.user.StubWriteUserPort;
import Dummy.Stub.application.out.s3.StubS3Port;
import Dummy.UserDummyData;
import Dummy.domain.StubUser;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetUserInfoUsecase.*;
import com.syncd.application.port.in.UpdateUserInfoUsecase.*;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;

import com.syncd.application.port.out.autentication.AuthenticationPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.application.service.ProjectService;
import com.syncd.application.service.UserService;
import com.syncd.domain.user.User;
import com.syncd.mapper.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private StubReadUserPort readUserPort;

    private StubReadProjectPort readProjectPort;

    private StubWriteUserPort writeUserPort;

    private StubS3Port s3Port;

    @BeforeEach
    void setUp() {
        readUserPort = new StubReadUserPort();
        s3Port = new StubS3Port();
        writeUserPort = new StubWriteUserPort();
        readProjectPort = new StubReadProjectPort();

        userService = new UserService(readUserPort,readProjectPort,writeUserPort,s3Port);
    }

    @Test
    void getUserInfo() {
        // Given
        User stubUser = new StubUser();

        // When
        GetUserInfoResponseDto response = userService.getUserInfo(stubUser.getId());

        // Then
        System.out.println(response);
        assertNotNull(response);
        assertEquals(stubUser.getId(), response.userId());
        assertEquals(stubUser.getName(), response.name());
        assertEquals(stubUser.getProfileImg(), response.img());
        assertEquals(stubUser.getEmail(), response.email());
    }

    @Test
    void updateUserInfo() {
        // Given
        User stubUser = new StubUser();

        // When
        UpdateUserInfoResponseDto response = userService.updateUserInfo(stubUser.getId(), stubUser.getName(), UserDummyData.getImageFile());

        // Then
        assertNotNull(response);
        assertEquals(stubUser.getId(), response.userId());
        assertEquals(stubUser.getName(), response.name());
        assertEquals(stubUser.getProfileImg(), response.img());

    }

    @Test
    void updateUserInfo_withoutNameChange() {
        // Given
        User stubUser = new StubUser();

        // When
        UpdateUserInfoResponseDto response = userService.updateUserInfo(stubUser.getId(), stubUser.getName(), UserDummyData.getImageFile());
        // Then
        assertNotNull(response);
        assertEquals(stubUser.getId(), response.userId());
        assertEquals(stubUser.getName(), response.name());
        assertEquals(stubUser.getProfileImg(), response.img());

    }

    @Test
    void updateUserInfo_withoutImageChange() {
        // Given
        User stubUser = new StubUser();

        // When
        UpdateUserInfoResponseDto response = userService.updateUserInfo(stubUser.getId(), stubUser.getName(), UserDummyData.getImageFile());

        // Then
        assertNotNull(response);
        assertEquals(stubUser.getId(), response.userId());
        assertEquals(stubUser.getName(), response.name());
        assertEquals(stubUser.getProfileImg(), response.img());

    }
}
