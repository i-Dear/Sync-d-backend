package com.syncd.application.port.out.gmail;

import org.springframework.scheduling.annotation.Async;

public interface SendMailPort {
    @Async
    String sendInviteMail(String email,String hostName, String userName, String projectName);
}
