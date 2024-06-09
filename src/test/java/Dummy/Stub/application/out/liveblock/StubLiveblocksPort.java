package Dummy.Stub.application.out.liveblock;

import Dummy.ProjectDummyData;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.dto.LiveblocksTokenDto;

import java.util.List;

public class StubLiveblocksPort implements LiveblocksPort {
    @Override
    public LiveblocksTokenDto GetRoomAuthToken(String userId, String name, String img, List<String> projectIds) {
        return new LiveblocksTokenDto(ProjectDummyData.LiveblocksToken.getValue());
    }
}
