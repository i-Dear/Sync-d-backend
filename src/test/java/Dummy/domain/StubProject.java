package Dummy.domain;

import Dummy.ProjectDummyData;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StubProject extends Project {
    public StubProject() {
        this.setId(ProjectDummyData.ProjectId.getValue());
        UserInProject user1 = new UserInProject(ProjectDummyData.User1Id.getValue(), Role.HOST);
        UserInProject user2 = new UserInProject(ProjectDummyData.User2Id.getValue(), Role.HOST);
        List<UserInProject> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        this.addUsers(users);

        // Set default values
        this.setImg(ProjectDummyData.ProjectImage.getValue()); // 이미지 기본값 설정
        this.setProgress(0); // 진행률 기본값 설정
        this.setLastModifiedDate(LocalDateTime.now().toString()); // 마지막 수정 날짜 기본값 설정
        this.setLeftChanceForUserstory(3); // 남은 유저 스토리 기회 기본값 설정
    }

    @Override
    public String getHost() {
        return ProjectDummyData.HostId.getValue();
    }

}
