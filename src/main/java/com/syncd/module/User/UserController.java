package com.syncd.module.User;

import com.syncd.module.User.dto.LoginDto;
import com.syncd.module.User.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String resgisterUser(@RequestBody RegisterDto registerDto){
        return userService.resgisterUser(registerDto);
    }

    @PostMapping("/login")
    public Boolean loginUser(@RequestBody LoginDto loginDto){
        return userService.loginUser(loginDto);
    }
}
