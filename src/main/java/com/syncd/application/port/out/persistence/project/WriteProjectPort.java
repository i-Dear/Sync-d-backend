package com.syncd.application.port.out.persistence.project;

import com.syncd.domain.project.Project;

public interface WriteProjectPort {
    String CreateProject(Project project);

    void RemoveProject(String projectId);

    String UpdateProject(Project project);
}
