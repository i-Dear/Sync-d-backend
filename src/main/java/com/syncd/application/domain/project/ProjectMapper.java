package com.syncd.application.domain.project;


import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto toGetAllRoomsByUserIdResponseDto(Project project);

    Project fromProjectDto(ProjectDto projectDto);

    GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto toProjectForGetAllInfoAboutRoomsByUserIdResponseDto(Project project);
    Project fromCreateProjectRequestDto(CreateProjectUsecase.CreateProjectRequestDto requestDto);
    //    OrganizationUsecase.GetRoomAuthTokenResponseDto fromLiveblocks(LiveblocksPort.RoomAuthToken roomAuthToken);


}
