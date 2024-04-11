package com.syncd.application.domain.user;


import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.out.autentication.dto.UserForTokenDto;
import com.syncd.application.port.out.persistence.project.dto.UserRoleForProjectDto;
import com.syncd.application.port.out.persistence.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromRegisterUserRequestDto(RegitsterUserUsecase.RegisterUserRequestDto requestDto);
    UserForTokenDto toUserForTokenDto(User user);

    UserDto UserDtoFromEntity(UserEntity userEntity);

    UserRoleForProjectDto toUserRoleForTeamDto(User user);
}
