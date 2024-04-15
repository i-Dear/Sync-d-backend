package com.syncd.application.port.out.liveblock;

import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.dto.GetRoomAuthTokenDto;
import com.syncd.application.port.out.liveblock.dto.UserRoleForLiveblocksDto;

import java.util.List;

public interface LiveblocksPort {
    GetRoomAuthTokenDto GetRoomAuthToken(String userId,List<UserRoleForLiveblocksDto> roles);
    GetRoomAuthTokenDto Test(String userId, String roomId);
}

