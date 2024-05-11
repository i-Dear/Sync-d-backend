package com.syncd.adapter.out.persistence;

import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.ProjectMapper;
import com.syncd.exceptions.ProjectAlreadyExistsException;
import com.syncd.exceptions.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements WriteProjectPort, ReadProjectPort {
    private final ProjectDao projectDao;

    @Override
    public List<Project> findAllProjectByUserId(String userId){
        List<ProjectEntity> projectEntityList = projectDao.findByUsersUserId(userId);
        List<Project> projects = projectEntityList.stream()
                .map(ProjectMapper.INSTANCE::fromProjectEntity)
                .collect(Collectors.toList());

        return projects;
    }

    @Override
    public Project findProjectByProjectId(String projectId){
        return projectDao.findById(projectId)
                .map(ProjectMapper.INSTANCE::fromProjectEntity)
                .orElseThrow(() -> new ProjectNotFoundException("No project found with ID: " + projectId));
    }

    @Override
    public String CreateProject(Project project) {
        if (project.getId() != null && projectDao.existsById(project.getId())) {
            throw new ProjectAlreadyExistsException(project.getId());
        }
        ProjectEntity projectEntity = ProjectMapper.INSTANCE.toProjectEntity(project);
        ProjectEntity savedProjectEntity = projectDao.save(projectEntity);
        return savedProjectEntity.getId();
    }

    @Override
    public void RemoveProject(String projectId){
        if (!projectDao.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        projectDao.deleteById(projectId);
    }

    @Override
    public String UpdateProject(Project project) {
        if (project.getId() == null || !projectDao.existsById(project.getId())) {
            throw new ProjectNotFoundException(project.getId());
        }
        ProjectEntity projectEntity = ProjectMapper.INSTANCE.toProjectEntity(project);
        ProjectEntity savedEntity = projectDao.save(projectEntity);
        return savedEntity.getId();
    }

    @Override
    public String AddProgress(String projectId, int projectStage) {
        ProjectEntity projectEntity = projectDao.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("No project found with ID: " + projectId));
        projectEntity.setProgress(projectStage);
        ProjectEntity savedEntity = projectDao.save(projectEntity);
        return savedEntity.getId();
    }

    public String updateLastModifiedDate(String projectId) {
        ProjectEntity projectEntity = projectDao.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("No project found with ID: " + projectId));
        projectEntity.setLastModifiedDate(LocalDateTime.now().toString()); // 현재 시간 설정
        ProjectEntity savedEntity = projectDao.save(projectEntity);
        return savedEntity.getId();
    }
}
