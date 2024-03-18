package com.syncd.domain.Room;

import com.syncd.domain.Room.dto.GetRoomAuthTokenDto;
import com.syncd.domain.User.UserService;
import com.syncd.domain.User.dto.LoginDto;
import com.syncd.domain.User.dto.RegisterDto;
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
    public String getRoomAuthToken(@RequestBody GetRoomAuthTokenDto getRoomAuthToken){
        return roomService.getRoomAuthToken(getRoomAuthToken);
    }

}
