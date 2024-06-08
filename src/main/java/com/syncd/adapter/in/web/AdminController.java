package com.syncd.adapter.in.web;

import com.syncd.application.port.in.admin.LoginAdminUsecase.*;
import com.syncd.application.port.in.admin.CreateAdminUsecase.*;
import com.syncd.application.port.in.admin.CreateProjectAdminUsecase.*;
import com.syncd.application.port.in.admin.DeleteProjectAdminUsecase.*;
import com.syncd.application.port.in.admin.UpdateProjectAdminUsecase.*;
import com.syncd.application.port.in.admin.SearchUserAdminUsecase.*;
import com.syncd.application.port.in.admin.SearchProjectAdminUsecase.*;
import com.syncd.application.port.in.admin.*;
import com.syncd.application.port.in.admin.CreateUserAdminUsecase.*;
import com.syncd.application.port.in.admin.DeleteUserAdminUsecase.*;
import com.syncd.application.port.in.admin.GetAllProjectAdminUsecase.*;
import com.syncd.application.port.in.admin.GetAllUserAdminUsecase.*;
import com.syncd.application.port.in.admin.GetChatgptPriceAdminUsecase.*;
import com.syncd.application.port.in.admin.UpdateUserAdminUsecase.*;
import com.syncd.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final LoginAdminUsecase loginAdminUsecase;
    private final CreateAdminUsecase createAdminUsecase;
    private final GetAllUserAdminUsecase getAllUserAdminUsecase;
    private final CreateUserAdminUsecase createUserAdminUsecase;
    private final CreateProjectAdminUsecase createProjectAdminUsecase;
    private final DeleteProjectAdminUsecase deleteProjectAdminUsecase;
    private final GetChatgptPriceAdminUsecase getChatgptPriceAdminUsecase;
    private final GetAllProjectAdminUsecase getAllProjectAdminUsecase;
    private final UpdateProjectAdminUsecase updateProjectAdminUsecase;
    private final UpdateUserAdminUsecase updateUserAdminUsecase;
    private final DeleteUserAdminUsecase deleteUserAdminUsecase;
    private final SearchUserAdminUsecase searchUserAdminUsecase;
    private final SearchProjectAdminUsecase searchProjectAdminUsecase;
    private final JwtService jwtService;
    // ======================================
    // USER
    // ======================================

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        return loginAdminUsecase.login(requestDto.email(), requestDto.password());
    }

    @PostMapping("/create")
    public CreateAdminResponseDto createAdmin(@RequestBody CreateAdminRequestDto requestDto) {
        return createAdminUsecase.createAdmin(requestDto.email(), requestDto.password(), requestDto.name());
    }

    @GetMapping("/user")
    public GetAllUserResponseDto getAllUser(HttpServletRequest request){
        return getAllUserAdminUsecase.getAllUser();
    }

    @PostMapping("/user/add")
    public CreateUserResponseDto addUser(HttpServletRequest request, @RequestBody CreateUserRequestDto requestDto){
        return createUserAdminUsecase.addUser(requestDto.email(),
                requestDto.name(), requestDto.status(),
                requestDto.profileImg(), requestDto.projectIds());
    }

    @PostMapping("/user/delete")
    public DeleteUserResponseDto deleteUser(HttpServletRequest request, @RequestBody DeleteUserRequestDto requestDto){
        return deleteUserAdminUsecase.deleteUser(requestDto.userId());
    }

    @PostMapping("/user/update")
    public UpdateUserResponseDto updateUser(HttpServletRequest request, @RequestBody UpdateUserRequestDto requestDto){
        return updateUserAdminUsecase.updateUser(requestDto.userId(), requestDto.email(),requestDto.name(), requestDto.status(), requestDto.profileImg(), requestDto.projectIds());
    }

    @GetMapping("/user/search")
    public SearchUserAdminResponseDto searchUsers(
            HttpServletRequest request,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchText) {
        return searchUserAdminUsecase.searchUsers(status, searchType, searchText);
    }
    // ======================================
    // PROJECT
    // ======================================

    @GetMapping("/project")
    public GetAllProjectResponseDto getAllProject(HttpServletRequest request){
        return getAllProjectAdminUsecase.getAllProject();
    }

    @PostMapping("/project/create")
    public CreateProjectAdminResponseDto createProject(HttpServletRequest request, @RequestBody CreateProjectAdminRequestDto requestDto){
        return createProjectAdminUsecase.createProject(requestDto.name(), requestDto.description(), requestDto.img(), requestDto.users(), requestDto.progress(),requestDto.leftChanceForUserstory());
    }

    @PostMapping("/project/delete")
    public DeleteProjectAdminResponseDto deleteProject(HttpServletRequest request, @RequestBody DeleteProjectAdminRequestDto requestDto){
        return deleteProjectAdminUsecase.deleteProject(requestDto.projectId());
    }

    @PostMapping("/project/update")
    public UpdateProjectAdminResponseDto updateProject(HttpServletRequest request, @RequestBody UpdateProjectAdminRequestDto requestDto){
        return updateProjectAdminUsecase.updateProject(requestDto.projectId(), requestDto.name(), requestDto.description(),requestDto.img(),requestDto.users(),requestDto.progress(),requestDto.leftChanceForUserstory()) ;
    }

    @GetMapping("/project/search")
    public SearchProjectAdminResponseDto searchProjects(
            HttpServletRequest request,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Integer leftChanceForUserstory,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer progress,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return searchProjectAdminUsecase.searchProjects(name, userId, leftChanceForUserstory, startDate, endDate, progress, page, pageSize);
    }

    // ======================================
    // CHATGPT
    // ======================================
    @GetMapping("/chatgpt")
    public GetChatgptPriceResponseDto GetChatgptPrice(HttpServletRequest request){
        return getChatgptPriceAdminUsecase.getChatgptPrice();
    }
}
