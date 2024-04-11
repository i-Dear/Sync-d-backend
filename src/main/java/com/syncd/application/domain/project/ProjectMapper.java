package com.syncd.application.domain.project;


import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto toGetAllRoomsByUserIdResponseDto(Project project);

//    OrganizationUsecase.GetRoomAuthTokenResponseDto fromLiveblocks(LiveblocksPort.RoomAuthToken roomAuthToken);
}
