package Dummy;

public enum Consistent {
    ProjectId("DummyProjectId"),
    UserId("DummyUserId"),
    UserName("DummyUserName"),
    ProjectName("DummyProjectName"),
    LiveblocksToken("DummyLiveblocksToken"),
    S3Link("DummyS3Link");


    private final String value;

    Consistent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
