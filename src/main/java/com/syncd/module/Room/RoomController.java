package com.syncd.module.Room;

import com.syncd.module.Room.dto.GetAllInfoAboutRoomsByUserIdRequestDto;
import com.syncd.module.Room.dto.GetAllInfoAboutRoomsByUserIdResponseDto;
import com.syncd.module.Room.dto.GetRoomAuthTokenRequestDto;
import com.syncd.module.Room.dto.GetRoomAuthTokenResponsetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/auth")
    public GetRoomAuthTokenResponsetDto getRoomAuthToken(@RequestBody GetRoomAuthTokenRequestDto getRoomAuthToken){
        return roomService.getRoomAuthToken(getRoomAuthToken);
    }

    @PostMapping("/")
    public GetAllInfoAboutRoomsByUserIdResponseDto getAllInfoAboutRoomsByUserId(@RequestBody GetAllInfoAboutRoomsByUserIdRequestDto getAllInfoAboutRoomsByUserIdRequestDto){
        return roomService.getAllInfoAboutRoomsByUserId(getAllInfoAboutRoomsByUserIdRequestDto);
    }
}
