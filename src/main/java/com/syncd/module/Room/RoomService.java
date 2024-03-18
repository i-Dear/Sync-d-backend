package com.syncd.domain.Room;

import com.syncd.domain.Room.dto.GetRoomAuthTokenDto;
import com.syncd.domain.User.UserDao;
import com.syncd.domain.User.dto.LoginDto;
import com.syncd.domain.User.dto.RegisterDto;
import com.syncd.domain.User.entity.UserEntity;
import com.syncd.domain.User.exceptions.LoginException;
import com.syncd.domain.User.exceptions.RegisterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomDao roomDao;

    public String getRoomAuthToken(GetRoomAuthTokenDto getRoomAuthTokenDto){
        return "";
    }
}
