package com.syncd.application.domain.project;


import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.out.liveblock.dto.UserRoleForLiveblocksDto;
import com.syncd.application.port.out.persistence.project.dto.ProjectDto;
import com.syncd.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto toGetAllRoomsByUserIdResponseDto(Project project);
    Project fromProjectDto(ProjectDto projectDto);

    @Mapping(target = "role", expression = "java(extractUserRole(project, userId))")
//    @Mapping(target = "roomId", ignore = true)  // Assuming roomId is set elsewhere or not needed
    GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto toProjectForGetAllInfoAboutRoomsByUserIdResponseDto(Project project, String userId);

    default Role extractUserRole(Project project, String userId) {
        return project.getUsers().stream()
                .filter(userInTeam -> userId.equals(userInTeam.getUserId()))
                .findFirst()
                .map(UserInTeam::getRole)
                .orElse(null); // Or handle accordingly if a role must always be present
    }
    Project fromCreateProjectRequestDto(CreateProjectUsecase.CreateProjectRequestDto requestDto);


}


