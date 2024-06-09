package Dummy.Stub.application.out.persistence.project;

import Dummy.ProjectDummyData;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.domain.project.Project;

public class StubWriteProjectPort implements WriteProjectPort {


    @Override
    public String CreateProject(Project project) {
        return ProjectDummyData.ProjectId.getValue();
    }

    @Override
    public void RemoveProject(String projectId) {}

    @Override
    public String UpdateProject(Project project) {
        return project.getId();
    }

    @Override
    public String AddProgress(String projectId, int projectStage) {
        return projectId;
    }

    @Override
    public String updateLastModifiedDate(String projectId) {
        return projectId;
    }
}
