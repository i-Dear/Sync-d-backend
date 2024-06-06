package com.syncd.adapter.in.web;

import com.syncd.application.service.JwtService;
import com.syncd.application.port.in.*;
import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.JoinProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.application.port.in.GetResultPdfUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.SyncProjectUsecase.*;
import com.syncd.dto.MakeUserStoryReauestDto;
import com.syncd.dto.MakeUserStoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;

    private final CreateProjectUsecase createProjectUsecase;

    private final JoinProjectUsecase joinProjectUsecase;

    private final InviteUserInProjectUsecase inviteUserInProjectUsecase;

    private final WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;

    private final DeleteProjectUsecase deleteProjectUsecase;

    private final UpdateProjectUsecase updateProjectUsecase;

    private final SyncProjectUsecase syncProjectUsecase;

    private final MakeUserstoryUsecase makeUserstoryUsecase;

    private final GetResultPdfUsecase getResultPdfUsecase;

    private final JwtService jwtService;

    @PostMapping(value = "/create")
    public CreateProjectResponseDto createProject(HttpServletRequest request, @Valid @ModelAttribute CreateProjectRequestDto requestDto) {
        String token = jwtService.resolveToken(request);
        return createProjectUsecase.createProject(jwtService.getUserIdFromToken(token), jwtService.getUsernameFromToken(token), requestDto.name(), requestDto.description(), requestDto.img(), requestDto.userEmails());
    }

    @PostMapping("/join")
    public JoinProjectResponseDto joinProject(HttpServletRequest request, @Valid @RequestBody JoinProjectRequestDto requestDto) {
        String token = jwtService.resolveToken(request);
        return joinProjectUsecase.joinProject(jwtService.getUserIdFromToken(token), requestDto.projectId());
    }

    @PostMapping("/invite")
    public InviteUserInProjectResponseDto inviteUser(HttpServletRequest request, @Valid @RequestBody InviteUserInProjectRequestDto requestDto){
        String token = jwtService.resolveToken(request);
        return inviteUserInProjectUsecase.inviteUserInProject(jwtService.getUserIdFromToken(token),jwtService.getUsernameFromToken(token), requestDto.projectId(), requestDto.users());
    }

    @PostMapping("/withdraw")
    public WithdrawUserInProjectResponseDto withdrawUser(HttpServletRequest request,@Valid @RequestBody WithdrawUserInProjectRequestDto requestDto){
        String token = jwtService.resolveToken(request);
        return withdrawUserInProjectUsecase.withdrawUserInProject(jwtService.getUserIdFromToken(token), requestDto.projectId(), requestDto.users());
    }

    @PostMapping("/delete")
    public DeleteProjectResponseDto deleteProject(HttpServletRequest request,@Valid @RequestBody DeleteProjectRequestDto requestDto){
        String token = jwtService.resolveToken(request);
        return deleteProjectUsecase.deleteProject(jwtService.getUserIdFromToken(token), requestDto.projectId());
    }

    @PostMapping("/update")
    public UpdateProjectResponseDto updateProject(HttpServletRequest request, @Valid @RequestBody UpdateProjectRequestDto requestDto){
        String token = jwtService.resolveToken(request);
        return updateProjectUsecase.updateProject(jwtService.getUserIdFromToken(token), requestDto.projectId(), requestDto.projectName(), requestDto.description(), requestDto.image());
    }

    @PostMapping("/sync")
    public SyncProjectResponseDto syncProject(HttpServletRequest request,
                                              @Valid @ModelAttribute SyncProjectRequestDto requestDto){
        String token = jwtService.resolveToken(request);
        return syncProjectUsecase.syncProject(jwtService.getUserIdFromToken(token),
                requestDto.projectId(),
                requestDto.projectStage(),
                requestDto.problem(),
                requestDto.personaImage(),
                requestDto.whyWhatHowImage(),
                requestDto.coreDetails(),
                requestDto.businessModelImage(),
                requestDto.epics(),
                requestDto.menuTreeImage());
    }

    @PostMapping("/userstory")
    public ResponseEntity<MakeUserStoryResponseDto> makeUserStory(HttpServletRequest request, @Valid @RequestBody MakeUserStoryReauestDto makeUserStoryReauestDto) {
        String token = jwtService.resolveToken(request);
        MakeUserStoryResponseDto result = makeUserstoryUsecase.makeUserstory(jwtService.getUserIdFromToken(token), makeUserStoryReauestDto.getProjectId(), makeUserStoryReauestDto.getScenario());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/result")
    public GetResultPdfUsecaseResponseDto getResultPdf(
            HttpServletRequest request,
            @RequestParam String projectId) {
        String token = jwtService.resolveToken(request);
        return getResultPdfUsecase.getResultPdfUsecaseProject(
                jwtService.getUserIdFromToken(token),
                projectId);
    }

}
