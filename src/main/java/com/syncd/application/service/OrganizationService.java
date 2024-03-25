package com.syncd.application.service;

import com.syncd.application.domain.Organization;
import com.syncd.application.port.in.OrganizationUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.organization.ReadOrganizationPort;
import com.syncd.application.port.out.organization.WriteOrganizationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class OrganizationService implements OrganizationUsecase {
    private final LiveblocksPort liveblocksPort;
    private final ReadOrganizationPort readOrganizationPort;
    private final WriteOrganizationPort writeOrganizationPort;

    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        String token = liveblocksPort.GetRoomAuthToken();
        return new GetRoomAuthTokenResponseDto(token);
    }

    public GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(GetAllInfoAboutRoomsByUserIdRequestDto requestDto) {
        return null;
    }


}
