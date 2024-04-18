package com.syncd.application.port.out.liveblock;

import com.syncd.dto.LiveblocksTokenDto;
import com.syncd.dto.UserRoleDto;

import java.util.List;

public interface LiveblocksPort {
    LiveblocksTokenDto GetRoomAuthToken(String userId, List<UserRoleDto> roles);
    LiveblocksTokenDto Test(String userId, String roomId);
}

