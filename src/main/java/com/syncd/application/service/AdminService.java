package com.syncd.application.service;

import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.admin.*;
import com.syncd.enums.UserAccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class AdminService implements CreateProjectAdminUsecase, CreateUserAdminUsecase, DeleteProjectAdminUsecase,
        DeleteUserAdminUsecase, GetAllProjectAdminUsecase, GetAllUserAdminUsecase, UpdateProjectAdminUsecase, UpdateUserAdminUsecase, GetChatgptPriceAdminUsecase {
    private final ProjectDao projectDao;
    private final UserDao userDao;
    @Override
    public CreateProjectAdminResponseDto createProject( String name,
                                                        String description,
                                                        String img,
                                                        List<CreateProjectAdminUsecase.UserInProjectRequestDto> users,
                                                        int progress,
                                                        int leftChanceForUserstory){

        ProjectEntity project = new ProjectEntity();
        project.setName(name);
        project.setDescription(description);
        project.setImg(new String(img.getBytes()));


        List<ProjectEntity.UserInProjectEntity> userEntities = users.stream()
                .map(user -> {
                    ProjectEntity.UserInProjectEntity entityUser = new ProjectEntity.UserInProjectEntity();
                    entityUser.setUserId(user.userId());
                    entityUser.setRole(user.role());
                    return entityUser;
                })
                .collect(Collectors.toList());

        project.setUsers(userEntities);
        project.setProgress(progress);
        project.setLastModifiedDate(java.time.LocalDateTime.now().toString()); // Assuming current time as last modified date
        project.setLeftChanceForUserstory(leftChanceForUserstory);

        ProjectEntity savedProject = projectDao.save(project);
        return new CreateProjectAdminResponseDto(savedProject.getId());
    }
    @Override
    public CreateUserResponseDto addUser(String email, String name, UserAccountStatus status, String profileImg, List<String> projectIds) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(name);
        user.setStatus(status);
        user.setProfileImg(profileImg);
        user.setProjectIds(projectIds);

        UserEntity savedUser = userDao.save(user);
        return new CreateUserResponseDto(savedUser.getId());
    }
    @Override
    public DeleteProjectAdminResponseDto deleteProject(String projectId) {
        Optional<ProjectEntity> projectOpt = projectDao.findById(projectId);

        if (projectOpt.isPresent()) {
            projectDao.deleteById(projectId);
            return new DeleteProjectAdminResponseDto(projectId);
        } else {
            throw new RuntimeException("Project not found.");
        }
    }

    @Override
    public DeleteUserResponseDto deleteUser(String userId) {
        Optional<UserEntity> userOpt = userDao.findById(userId);
        if (userOpt.isPresent()) {
            userDao.delete(userOpt.get());
            return new DeleteUserResponseDto(userId);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    public GetAllProjectResponseDto getAllProject() {
        List<ProjectEntity> projects = projectDao.findAll();
        return new GetAllProjectResponseDto(projects);
    }

    @Override
    public GetAllUserResponseDto getAllUser() {
        List<UserEntity> users = userDao.findAll();
        return new GetAllUserResponseDto(users);
    }

    @Override
    public UpdateProjectAdminResponseDto updateProject(String projectId, String name, String description,
                                                       String img, List<UpdateProjectAdminUsecase.UserInProjectRequestDto> users,
                                                       int progress, int leftChanceForUserstory) {

        Optional<ProjectEntity> projectOpt = projectDao.findById(projectId);
        if (projectOpt.isPresent()) {
            ProjectEntity project = projectOpt.get();
            project.setName(name);
            project.setDescription(description);
            project.setImg(new String(img.getBytes()));

            List<ProjectEntity.UserInProjectEntity> userEntities = users.stream()
                    .map(user -> {
                        ProjectEntity.UserInProjectEntity entityUser = new ProjectEntity.UserInProjectEntity();
                        entityUser.setUserId(user.userId());
                        entityUser.setRole(user.role());
                        return entityUser;
                    })
                    .collect(Collectors.toList());

            project.setUsers(userEntities);
            project.setProgress(progress);
            project.setLastModifiedDate(java.time.LocalDateTime.now().toString()); // Assuming current time as last modified date
            project.setLeftChanceForUserstory(leftChanceForUserstory);

            ProjectEntity updatedProject = projectDao.save(project);
            return new UpdateProjectAdminResponseDto(updatedProject.getId());
        } else {
            throw new RuntimeException("Project not found.");
        }
    }

    @Override
    public UpdateUserResponseDto updateUser(String userId, String email, String name, UserAccountStatus status,
                                            String profileImg, List<String> projectIds) {

        Optional<UserEntity> userOpt = userDao.findById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setEmail(email);
            user.setName(name);
            user.setStatus(status);
            user.setProfileImg(profileImg);
            user.setProjectIds(projectIds);

            UserEntity updatedUser = userDao.save(user);
            return new UpdateUserResponseDto(updatedUser);
        } else {
            throw new RuntimeException("User not found.");
        }
    }
    @Override
    public GetChatgptPriceResponseDto getChatgptPrice(){
        return new GetChatgptPriceResponseDto("3.2","4.5","10.4","11.2","40.3");
    }
}
