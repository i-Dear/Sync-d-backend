package com.syncd.application.domain;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.syncd.application.port.in.OrganizationUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

//    Organization from(OrganizationUsecase.GetRoomAuthTokenResponseDto d);
//    OrganizationUsecase.GetRoomAuthTokenResponseDto fromLiveblocks(LiveblocksPort.RoomAuthToken roomAuthToken);
}
