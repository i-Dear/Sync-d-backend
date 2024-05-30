package com.syncd.application.service;

import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.admin.*;
import com.syncd.enums.UserAccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class AdminService implements CreateProjectAdminUsecase, CreateUserAdminUsecase, DeleteProjectAdminUsecase,
        DeleteUserAdminUsecase, GetAllProjectAdminUsecase, GetAllUserAdminUsecase, UpdateProjectAdminUsecase, UpdateUserAdminUsecase, GetChatgptPriceAdminUsecase, SearchUserAdminUsecase, SearchProjectAdminUsecase {
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
    public SearchUserAdminResponseDto searchUsers(String status, String searchType, String searchText) {
        List<UserEntity> users = userDao.findAll();

        List<SearchUserAdminUsecase.UserWithProjectsDto> userWithProjects = users.stream()
                .filter(user -> (status == null || user.getStatus().toString().equalsIgnoreCase(status)) &&
                        (searchType == null || searchText == null ||
                                (searchType.equals("userName") && user.getName().contains(searchText)) ||
                                (searchType.equals("email") && user.getEmail().contains(searchText)) ||
                                (searchType.equals("projectId") && user.getProjectIds().contains(searchText))))
                .map(user -> {
                    List<ProjectEntity> userProjects = user.getProjectIds().stream()
                            .map(projectId -> projectDao.findById(projectId).orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return new SearchUserAdminUsecase.UserWithProjectsDto(user, userProjects);
                })
                .collect(Collectors.toList());

        return new SearchUserAdminResponseDto(userWithProjects);
    }


    @Override
    public SearchProjectAdminResponseDto searchProjects(
            String name,
            String userId,
            Integer leftChanceForUserstory,
            String startDate,
            String endDate,
            Integer progress,
            int page,
            int pageSize
    ) {
        DateTimeFormatter requestFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        List<ProjectEntity> projects = projectDao.findAll();

        // Filtering logic
        if (name != null && !name.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> name.equalsIgnoreCase(project.getName()))
                    .collect(Collectors.toList());
        }
        if (userId != null && !userId.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> project.getUsers().stream().anyMatch(user -> userId.equals(user.getUserId())))
                    .collect(Collectors.toList());
        }
        if (leftChanceForUserstory != null) {
            projects = projects.stream()
                    .filter(project -> leftChanceForUserstory.intValue() == project.getLeftChanceForUserstory())
                    .collect(Collectors.toList());
        }
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(startDate, requestFormatter);
            LocalDateTime end = LocalDateTime.parse(endDate, requestFormatter);
            projects = projects.stream()
                    .filter(project -> {
                        LocalDateTime lastModifiedDate = LocalDateTime.parse(project.getLastModifiedDate(), dataFormatter);
                        return (lastModifiedDate.isAfter(start) || lastModifiedDate.isEqual(start)) &&
                                (lastModifiedDate.isBefore(end) || lastModifiedDate.isEqual(end));
                    })
                    .collect(Collectors.toList());
        }
        if (progress != null) {
            projects = projects.stream()
                    .filter(project -> progress.intValue() == project.getProgress())
                    .collect(Collectors.toList());
        }

        // Fetch user details for each project and create a user map
        Map<String, UserEntity> userMap = new HashMap<>();
        for (ProjectEntity project : projects) {
            for (ProjectEntity.UserInProjectEntity userInProject : project.getUsers()) {
                userDao.findById(userInProject.getUserId()).ifPresent(userEntity -> userMap.put(userEntity.getId(), userEntity));
            }
        }

        // Pagination logic
        long totalCount = projects.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, projects.size());
        List<ProjectEntity> paginatedProjects = projects.subList(startIndex, endIndex);

        return new SearchProjectAdminResponseDto(paginatedProjects, totalCount, userMap);
    }

    @Override
    public GetChatgptPriceResponseDto getChatgptPrice(){
        return new GetChatgptPriceResponseDto("3.2","4.5","10.4","11.2","40.3");
    }
}
