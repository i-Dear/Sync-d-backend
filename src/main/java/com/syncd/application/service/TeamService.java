package com.syncd.application.service;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.persistence.team.ReadTeamPort;
import com.syncd.application.port.out.persistence.team.WriteTeamPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class TeamService implements GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadTeamPort readTeamPort;
    private final WriteTeamPort writeTeamPort;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        String token = liveblocksPort.GetRoomAuthToken();
        return new GetRoomAuthTokenResponseDto(token);
    }

    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(GetAllRoomsByUserIdRequestDto requestDto) {
        return null;
    }


}
