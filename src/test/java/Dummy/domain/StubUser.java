package Dummy.domain;

import Dummy.UserDummyData;
import com.syncd.domain.user.User;

public class StubUser extends User {
    public StubUser() {
        this.setId(UserDummyData.UserId.getValue());
        this.setName(UserDummyData.UserName.getValue());
        this.setEmail(UserDummyData.UserEmail.getValue());
        this.setProfileImg(UserDummyData.UserImage.getValue());
        this.setNumberOfLeftHostProjects(1);
    }

}