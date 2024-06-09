package Dummy;

import org.springframework.web.multipart.MultipartFile;

public enum UserDummyData {
    UserId("DummyUser1Id"),
    UserName("DummyUser1Name"),
    UserEmail("DummyUser1Email@gmail.com"),
    UserImage("DummyUser1Image");


    private final String value;

    UserDummyData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MultipartFile getImageFile(){
        byte[] imageContent = "userImage".getBytes();
        return new StubMultipartFile("userImage", "userImage.jpg", "image/jpeg", imageContent);
    }


}
