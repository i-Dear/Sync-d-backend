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
    public void testFromRegisterUserRequestDto() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("testUser", "test@example.com", "password");

        User user = userMapper.mapRegisterUserRequestDtoToUser(requestDto);

        assertNotNull(user);
        assertEquals("testUser", user.getName());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testToUserForTokenDto() {
        User user = new User();
        user.setId("1");
        user.setName("testUser");
        user.setEmail("test@example.com");
        user.setProfileImg("profileImg.jpg");

        UserForTokenDto userForTokenDto = userMapper.mapUserToUserForTokenDto(user);

        assertNotNull(userForTokenDto);
        assertEquals("1", userForTokenDto.userId());
    }

    @Test
    public void testFromEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName("testUser");
        userEntity.setEmail("test@example.com");
        userEntity.setProfileImg("profileImg.jpg");

        User user = userMapper.mapUserEntityToUser(userEntity);

        assertNotNull(user);
        assertEquals("1", user.getId());
        assertEquals("testUser", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("profileImg.jpg", user.getProfileImg());
    }

    @Test
    public void testFromDto() {
        UserDto userDto = new UserDto("1", "test@example.com", "testUser", UserAccountStatus.AVAILABLE, "profileImg.jpg", Arrays.asList("proj1", "proj2"));

        User user = userMapper.mapUserDtoToUser(userDto);

        assertNotNull(user);
        assertEquals("1", user.getId());
        assertEquals("testUser", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("profileImg.jpg", user.getProfileImg());

    }

    @Test
    public void testToDto() {
        User user = new User();
        user.setId("1");
        user.setName("testUser");
        user.setEmail("test@example.com");
        user.setProfileImg("profileImg.jpg");

        UserDto userDto = userMapper.mapUserToUserDto(user);

        assertNotNull(userDto);
        assertEquals("1", userDto.id());
        assertEquals("testUser", userDto.name());
        assertEquals("test@example.com", userDto.email());
        assertEquals("profileImg.jpg", userDto.profileImg());
    }

    @Test
    public void testFromDtoWithNullProjectIds() {
        UserDto userDto = new UserDto("1", "test@example.com", "testUser", UserAccountStatus.AVAILABLE, "profileImg.jpg", null);

        User user = userMapper.mapUserDtoToUser(userDto);

        assertNotNull(user);
        assertEquals("1", user.getId());
        assertEquals("testUser", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("profileImg.jpg", user.getProfileImg());
    }

    @Test
    public void testToUserRoleForTeamDto() {
        User user = new User();
        user.setId("1");
        user.setName("testUser");

        UserRoleDto userRoleDto = userMapper.mapUserToUserRoleDto(user, "proj1", Role.HOST);

        assertNotNull(userRoleDto);
        assertEquals("proj1", userRoleDto.projectId());
        assertEquals("1", userRoleDto.userId());
        assertEquals(Role.HOST, userRoleDto.role());
    }
}
