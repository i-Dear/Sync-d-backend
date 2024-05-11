package com.syncd.domain.project;


import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.application.port.in.CreateProjectUsecase;
import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    Project fromProjectEntity(ProjectEntity projectEntity);

    ProjectEntity toProjectEntity(Project project);
}


