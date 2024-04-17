package com.syncd.adapter.out.persistence;

import com.syncd.adapter.out.persistence.exception.ProjectAlreadyExistsException;
import com.syncd.adapter.out.persistence.exception.ProjectNotFoundException;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements WriteProjectPort, ReadProjectPort {
    private final ProjectDao projectDao;

    public List<Project> findAllProjectByUserId(String userId){
        List<ProjectEntity> projectEntityList = projectDao.findByUsersUserId(userId);
        List<Project> projects = projectEntityList.stream()
                .map(ProjectMapper.INSTANCE::fromProjectEntity)
                .collect(Collectors.toList());

        return projects;
    }

    public Project findProjectByProjectId(String projectId){
        ProjectEntity projectEntity = projectDao.findById(projectId).orElse(null);
        return ProjectMapper.INSTANCE.fromProjectEntity(projectEntity);
    }

    public String CreateProject(Project project) {
        if (project.getId() != null && projectDao.existsById(project.getId())) {
            throw new ProjectAlreadyExistsException(project.getId());
        }
        ProjectEntity projectEntity = ProjectMapper.INSTANCE.toProjectEntity(project);
        ProjectEntity savedProjectEntity = projectDao.save(projectEntity);
        return savedProjectEntity.getId();
    }

    public void RemoveProject(String projectId){
        projectDao.deleteById(projectId);
    }

    public String UpdateProject(Project project) {
        if (project.getId() == null || !projectDao.existsById(project.getId())) {
            throw new ProjectNotFoundException(project.getId());
        }
        ProjectEntity projectEntity = ProjectMapper.INSTANCE.toProjectEntity(project);
        ProjectEntity savedEntity = projectDao.save(projectEntity);
        return savedEntity.getId();
    }

}
