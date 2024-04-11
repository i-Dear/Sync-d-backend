package com.syncd.application.service;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadProjectPort readTeamPort;
    private final WriteProjectPort writeTeamPort;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        String token = liveblocksPort.GetRoomAuthToken();
        return new GetRoomAuthTokenResponseDto(token);
    }

    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        return null;
    }


}
