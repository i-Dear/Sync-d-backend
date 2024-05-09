package com.syncd.application.port.out.gmail;

public interface SendMailPort {
    String sendInviteMail(String email,String hostName, String userName, String projectName);
}
