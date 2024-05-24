package com.syncd.application.port.out.liveblock;

import com.syncd.dto.LiveblocksTokenDto;

import java.util.List;

public interface LiveblocksPort {
    LiveblocksTokenDto GetRoomAuthToken(String userId, String name,String img,List<String> projectIds);
}

