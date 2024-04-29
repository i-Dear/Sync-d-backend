package com.syncd.domain.user;


import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.dto.UserDto;
import com.syncd.dto.UserForTokenDto;
import com.syncd.dto.UserRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromRegisterUserRequestDto(RegitsterUserUsecase.RegisterUserRequestDto requestDto);
    UserForTokenDto toUserForTokenDto(User user);

    User fromEntity(UserEntity userEntity);

    User fromDto(UserDto dto);

    UserRoleDto toUserRoleForTeamDto(User user);
}
