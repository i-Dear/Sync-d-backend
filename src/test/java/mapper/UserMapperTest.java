package mapper;

import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.RegitsterUserUsecase.*;
import com.syncd.domain.user.User;
import com.syncd.dto.UserDto;
import com.syncd.dto.UserForTokenDto;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import com.syncd.enums.UserAccountStatus;
import com.syncd.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testMapRegisterUserRequestDtoToUser() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("testUser", "test@example.com", "password");
        User user = userMapper.mapRegisterUserRequestDtoToUser(requestDto);
        assertUserFields(user, "testUser", "test@example.com");
    }

    @Test
    public void testMapUserToUserForTokenDto() {
        User user = createUser("1", "testUser", "test@example.com", "profileImg.jpg");
        UserForTokenDto userForTokenDto = userMapper.mapUserToUserForTokenDto(user);
        assertNotNull(userForTokenDto);
        assertEquals("1", userForTokenDto.userId());
    }

    @Test
    public void testMapUserEntityToUser() {
        UserEntity userEntity = createUserEntity("1", "testUser", "test@example.com", "profileImg.jpg");
        User user = userMapper.mapUserEntityToUser(userEntity);
        assertUserFields(user, "1", "testUser", "test@example.com", "profileImg.jpg");
    }

    @Test
    public void testMapUserDtoToUser() {
        UserDto userDto = new UserDto("1", "test@example.com", "testUser", UserAccountStatus.AVAILABLE, "profileImg.jpg", Arrays.asList("proj1", "proj2"));
        User user = userMapper.mapUserDtoToUser(userDto);
        assertUserFields(user, "1", "testUser", "test@example.com", "profileImg.jpg");
    }

    @Test
    public void testMapUserToUserDto() {
        User user = createUser("1", "testUser", "test@example.com", "profileImg.jpg");
        UserDto userDto = userMapper.mapUserToUserDto(user);
        assertUserDtoFields(userDto, "1", "testUser", "test@example.com", "profileImg.jpg");
    }

    @Test
    public void testMapUserDtoToUserWithNullProjectIds() {
        UserDto userDto = new UserDto("1", "test@example.com", "testUser", UserAccountStatus.AVAILABLE, "profileImg.jpg", null);
        User user = userMapper.mapUserDtoToUser(userDto);
        assertUserFields(user, "1", "testUser", "test@example.com", "profileImg.jpg");
    }

    @Test
    public void testMapUserToUserRoleDto() {
        User user = createUser("1", "testUser", null, null);
        UserRoleDto userRoleDto = userMapper.mapUserToUserRoleDto(user, "proj1", Role.HOST);
        assertNotNull(userRoleDto);
        assertEquals("proj1", userRoleDto.projectId());
        assertEquals("1", userRoleDto.userId());
        assertEquals(Role.HOST, userRoleDto.role());
    }

    // Helper methods to create entities and assert fields
    private User createUser(String id, String name, String email, String profileImg) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setProfileImg(profileImg);
        return user;
    }

    private UserEntity createUserEntity(String id, String name, String email, String profileImg) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setProfileImg(profileImg);
        return userEntity;
    }

    private void assertUserFields(User user, String id, String name, String email, String profileImg) {
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(profileImg, user.getProfileImg());
    }

    private void assertUserFields(User user, String name, String email) {
        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    private void assertUserDtoFields(UserDto userDto, String id, String name, String email, String profileImg) {
        assertNotNull(userDto);
        assertEquals(id, userDto.id());
        assertEquals(name, userDto.name());
        assertEquals(email, userDto.email());
        assertEquals(profileImg, userDto.profileImg());
    }
}
