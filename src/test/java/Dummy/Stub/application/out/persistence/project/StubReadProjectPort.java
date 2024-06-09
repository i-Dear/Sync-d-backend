package Dummy.Stub.application.out.persistence.project;

import Dummy.domain.StubProject;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.domain.project.Project;

import java.util.ArrayList;
import java.util.List;

public class StubReadProjectPort implements ReadProjectPort {

    @Override
    public Project findProjectByProjectId(String projectId) {
        Project project = new StubProject();
        return project;
    }

    @Override
    public List<Project> findAllProjectByUserId(String userId) {
        Project project = new StubProject();
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        return projects;
    }
}
