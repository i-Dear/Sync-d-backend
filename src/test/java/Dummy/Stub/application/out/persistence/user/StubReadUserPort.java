package Dummy.Stub.application.out.persistence.user;

import Dummy.ProjectDummyData;
import Dummy.domain.StubUser;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class StubReadUserPort implements ReadUserPort {

    @Override
    public User findByEmail(String email) {
        return new StubUser();
    }

    @Override
    public User findByUsername(String username) {
        return new StubUser();
    }

    @Override
    public User findByUserId(String username) {
        return new StubUser();
    }

    @Override
    public boolean isExistUser(String email) {
        return false;
    }

    @Override
    public List<User> usersFromEmails(List<String> emails) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(ProjectDummyData.User1Id.getValue());
        user.setName(ProjectDummyData.User1Name.getValue());
        users.add(user);
        return users;
    }
}
