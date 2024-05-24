package com.syncd.mapper;


import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
import com.syncd.dto.UserDto;
import com.syncd.dto.UserForTokenDto;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profileImg", ignore = true)
    User mapRegisterUserRequestDtoToUser(RegitsterUserUsecase.RegisterUserRequestDto requestDto);

    @Mapping(source = "id", target = "userId")
    UserForTokenDto mapUserToUserForTokenDto(User user);

    User mapUserEntityToUser(UserEntity userEntity);

    User mapUserDtoToUser(UserDto dto);

    @Mapping(target = "projectIds", ignore = true)
    UserDto mapUserToUserDto(User user);

    default UserRoleDto mapUserToUserRoleDto(User user, String projectId, Role role) {
        return new UserRoleDto(projectId, user.getId(), role);
    }
    default List<UserInProject> mapEmailsToUserInProjectList(List<String> userEmails, String hostName, String projectName, String projectId) {
        return userEmails.stream()
                .map(email -> createUserInProjectWithRoleMember(email, hostName, projectName, projectId))
                .collect(Collectors.toList());
    }
    default UserInProject createUserInProjectWithRoleMember(String email, String hostName, String projectName, String projectId) {
        return new UserInProject(email, Role.MEMBER);
    }
}
