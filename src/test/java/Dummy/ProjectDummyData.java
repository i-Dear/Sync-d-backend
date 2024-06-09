package Dummy;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public enum ProjectDummyData {
    ProjectId("DummyProjectId"),
    ProjectDescription("DummyProjectDescription"),
    ProjectImage("DummyProjectImage"),
    User1Id("DummyUser1Id"),
    User2Id("DummyUser2Id"),
    HostId("DummyHostId"),
    HostName("DummyHostName"),
    User1Name("DummyUser1Name"),
    User2Name("DummyUser2Name"),
    User1Email("DummyUser1@gmail.com"),
    User2Email("DummyUser2@gmail.com"),
    ProjectName("DummyProjectName"),
    LiveblocksToken("DummyLiveblocksToken"),
    S3Link("DummyS3Link");


    private final String value;

    ProjectDummyData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getUserLists() {
        List<String> userLists = new ArrayList<>();
        userLists.add(User1Id.getValue());
        userLists.add(User2Id.getValue());
        return userLists;
    }

    public static MultipartFile getImageFile(){
        byte[] imageContent = "projectImage".getBytes();
        return new StubMultipartFile("projectImage", "projectImage.jpg", "image/jpeg", imageContent);
    }

}
