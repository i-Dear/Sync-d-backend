package Dummy.Stub.application.out.persistence.user;

import Dummy.Consistent;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class StubReadUserPort implements ReadUserPort {

    @Override
    public User findByEmail(String email) {
        User user = new User();
        user.setId(Consistent.UserId.getValue());
        user.setName(Consistent.UserName.getValue());
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setId(Consistent.UserId.getValue());
        user.setName(Consistent.UserName.getValue());
        return user;
    }

    @Override
    public User findByUserId(String username) {
        User user = new User();
        user.setId(Consistent.UserId.getValue());
        user.setName(Consistent.UserName.getValue());
        return user;
    }

    @Override
    public boolean isExistUser(String email) {
        if(email.equals("true@gmail.com")){
            return true;
        }
        return false;
    }

    @Override
    public List<User> usersFromEmails(List<String> emails) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(Consistent.UserId.getValue());
        user.setName(Consistent.UserName.getValue());
        users.add(user);
        return users;
    }
}
