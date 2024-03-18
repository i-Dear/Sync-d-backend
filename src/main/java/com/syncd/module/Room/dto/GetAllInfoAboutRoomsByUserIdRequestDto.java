package com.syncd.module.Room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllInfoAboutRoomsByUserIdDto {
    private String userId;
    private String roomId;
}
