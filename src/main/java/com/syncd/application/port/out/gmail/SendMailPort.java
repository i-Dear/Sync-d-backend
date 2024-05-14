package com.syncd.application.port.out.gmail;

import com.syncd.domain.user.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface SendMailPort {
    @Async
    String sendInviteMail(String email,String hostName, String userName, String projectName, String ProjectId);

    String sendIviteMailBatch(String hostName, String projectName, List<User> users, String ProjectId);
}
