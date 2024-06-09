package application.service;

import Dummy.Stub.application.out.persistence.project.StubReadProjectPort;
import Dummy.Stub.application.out.persistence.user.StubReadUserPort;
import Dummy.Stub.application.out.persistence.user.StubWriteUserPort;
import Dummy.Stub.application.out.s3.StubS3Port;
import Dummy.UserDummyData;
import Dummy.domain.StubUser;
import com.syncd.application.port.in.GetUserInfoUsecase.*;
import com.syncd.application.port.in.UpdateUserInfoUsecase.*;
import com.syncd.application.service.UserService;
import com.syncd.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.*;


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
