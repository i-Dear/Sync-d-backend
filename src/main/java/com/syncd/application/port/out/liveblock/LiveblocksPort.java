package com.syncd.application.port.out.liveblock;

import com.syncd.domain.project.Project;
import com.syncd.dto.LiveblocksTokenDto;
import com.syncd.dto.UserRoleDto;

import java.util.List;

public interface LiveblocksPort {
    LiveblocksTokenDto GetRoomAuthToken(String userId, String name,String img,List<String> projectIds);
    LiveblocksTokenDto Test(String userId, String roomId);
}

