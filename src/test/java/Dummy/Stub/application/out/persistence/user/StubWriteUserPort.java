package Dummy.Stub.application.out.persistence.user;

import Dummy.Consistent;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import com.syncd.dto.UserId;

public class StubWriteUserPort implements WriteUserPort {
    @Override
    public UserId createUser(String userName, String email, String img) {
        return new UserId(Consistent.UserId.getValue());
    }

    @Override
    public UserId updateUser(User user) {
        return new UserId(Consistent.UserId.getValue());
    }
}
