package com.syncd.application.domain.team;


import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto toGetAllRoomsByUserIdResponseDto(Team team);

//    OrganizationUsecase.GetRoomAuthTokenResponseDto fromLiveblocks(LiveblocksPort.RoomAuthToken roomAuthToken);
}
