package Dummy.Stub.application.out.gmail;

import Dummy.ProjectDummyData;
import com.syncd.application.port.out.gmail.SendMailPort;

import java.util.List;

public class StubSendMailPort implements SendMailPort {
    @Override
    public String sendInviteMail(String email, String hostName, String userName, String projectName, String ProjectId) {
        return ProjectDummyData.ProjectName.getValue();
    }

    @Override
    public String sendIviteMailBatch(String hostName, String projectName, List<String> userEmails, String ProjectId) {

        return ProjectDummyData.ProjectName.getValue();
    }
}
