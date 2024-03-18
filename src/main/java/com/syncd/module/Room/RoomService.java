package com.syncd.module.Room;

import com.syncd.domain.dao.*;
import com.syncd.domain.entity.*;
import com.syncd.module.Room.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private WorkspaceDao workspaceDao;
    @Autowired
    private WorkspacePermissionDao workspacePermissionDao;
    @Autowired
    private OrganizationPermissionDao organizationPermissionDao;

    public GetRoomAuthTokenResponsetDto getRoomAuthToken(GetRoomAuthTokenRequestDto getRoomAuthTokenRequestDto){
        return new GetRoomAuthTokenResponsetDto("token");
    }

    public GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(GetAllInfoAboutRoomsByUserIdRequestDto requestDto) {
        System.out.print("d");
        Optional<UserEntity> userOptional = userDao.findByEmail(requestDto.getEmail());

        if (!userOptional.isPresent()) {
            return new GetAllInfoAboutRoomsByUserIdResponseDto(requestDto.getEmail(), new ArrayList<>());
        }

        UserEntity user = userOptional.get();
        List<OrganizationEntity> organizations = organizationDao.findAllById(user.getOrganizationIds());

        // 미리 응답 DTO 객체 생성
        GetAllInfoAboutRoomsByUserIdResponseDto responseDto = GetAllInfoAboutRoomsByUserIdResponseDto.builder()
                .userId(user.getUserId())
                .organizations(new ArrayList<>()) // 초기 빈 리스트로 설정
                .build();

        // 스트림을 통해 각 조직 정보 처리
        organizations.forEach(organizationEntity -> {
            List<WorkspaceEntity> workspaceEntities = workspaceDao.findAllById(organizationEntity.getWorkspaceIds());
            List<OrganizationPermissionEntity> organizationPermissionEntities = organizationPermissionDao.findByKey_UserId(user.getUserId());
            List<WorkspacePermissionEntity> workspacePermissionEntities = workspaceEntities.stream()
                    .flatMap(workspaceEntity -> workspacePermissionDao.findByKey_UserIdAndKey_WorkspaceId(user.getUserId(), workspaceEntity.getWorkspaceId()).stream())
                    .collect(Collectors.toList());

            responseDto.createOrganizationFromEntity(
                    organizationEntity,
                    workspaceEntities,
                    organizationPermissionEntities,
                    workspacePermissionEntities);

        });

        return responseDto;
    }


}
