package com.syncd.adapter.out.liveblock;

import com.syncd.application.port.out.liveblock.LiveblocksPort;
import org.springframework.stereotype.Component;

@Component
public class LiveblockApiAdapter implements LiveblocksPort {
    @Override
    public String GetRoomAuthToken() {
        return "example token";
    }
}
