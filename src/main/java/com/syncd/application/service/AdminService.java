package com.syncd.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.syncd.adapter.out.persistence.repository.admin.AdminDao;
import com.syncd.adapter.out.persistence.repository.admin.AdminEntity;
import com.syncd.adapter.out.persistence.repository.project.ProjectDao;
import com.syncd.adapter.out.persistence.repository.project.ProjectEntity;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.in.admin.*;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.enums.UserAccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class AdminService implements LoginAdminUsecase, CreateAdminUsecase, CreateProjectAdminUsecase, CreateUserAdminUsecase, DeleteProjectAdminUsecase,
        DeleteUserAdminUsecase, GetAllProjectAdminUsecase, GetAllUserAdminUsecase, UpdateProjectAdminUsecase, UpdateUserAdminUsecase, GetChatgptPriceAdminUsecase, SearchUserAdminUsecase, SearchProjectAdminUsecase {
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final AdminDao adminDao;
    private final JwtService jwtService;
    private final S3Port s3Port;
    private final ObjectMapper objectMapper;

    @Override
    public LoginResponseDto login(String email, String password) {
        AdminEntity admin = adminDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!(password.equals(admin.getPassword()))) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateTokenForAdmin(admin);
        return new LoginResponseDto(token);
    }

    @Override
    public CreateAdminResponseDto createAdmin(String email, String password, String name) {
        AdminEntity admin = new AdminEntity();
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setName(name);

        AdminEntity savedAdmin = adminDao.save(admin);
        return new CreateAdminResponseDto(savedAdmin.getId());
    }

    @Override
    public CreateProjectAdminResponseDto createProject(String adminId, String name, String description, MultipartFile img, String usersJson, int progress, int leftChanceForUserstory) {
        List<CreateProjectAdminUsecase.UserInProjectRequestDto> users;
        try {
            users = objectMapper.readValue(usersJson, new TypeReference<List<CreateProjectAdminUsecase.UserInProjectRequestDto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse users JSON", e);
        }

        ProjectEntity project = new ProjectEntity();
        project.setName(name);
        project.setDescription(description);
        project.setImg(uploadFileToS3(img));

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
    public CreateUserResponseDto addUser(String adminId, String email, String name, String status, MultipartFile profileImg, String projectIdsJson) {
        List<String> projectIds = Collections.emptyList();
        if (projectIdsJson != null && !projectIdsJson.isEmpty() && !"undefined".equals(projectIdsJson)) {
            try {
                projectIds = objectMapper.readValue(projectIdsJson, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse projectIds JSON", e);
            }
        }

        UserAccountStatus userStatus;
        try {
            userStatus = UserAccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status, e);
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(name);
        user.setStatus(userStatus);
        user.setProfileImg(uploadFileToS3(profileImg));
        user.setProjectIds(projectIds);

        UserEntity savedUser = userDao.save(user);
        return new CreateUserResponseDto(savedUser.getId());
    }

    @Override
    public DeleteProjectAdminResponseDto deleteProject(String adminId, String projectId) {
        Optional<ProjectEntity> projectOpt = projectDao.findById(projectId);

        if (projectOpt.isPresent()) {
            projectDao.deleteById(projectId);
            return new DeleteProjectAdminResponseDto(projectId);
        } else {
            throw new RuntimeException("Project not found.");
        }
    }

    @Override
    public DeleteUserResponseDto deleteUser(String adminId, String userId) {
        Optional<UserEntity> userOpt = userDao.findById(userId);
        if (userOpt.isPresent()) {
            userDao.delete(userOpt.get());
            return new DeleteUserResponseDto(userId);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    public GetAllProjectResponseDto getAllProject(String adminId) {
        List<ProjectEntity> projects = projectDao.findAll();
        return new GetAllProjectResponseDto(projects);
    }

    @Override
    public GetAllUserResponseDto getAllUser(String adminId) {
        List<UserEntity> users = userDao.findAll();
        return new GetAllUserResponseDto(users);
    }

    @Override
    public UpdateProjectAdminResponseDto updateProject(String adminId, String projectId, String name, String description, MultipartFile img, String usersJson, int progress, int leftChanceForUserstory) {
        List<UpdateProjectAdminUsecase.UserInProjectRequestDto> users;
        try {
            users = objectMapper.readValue(usersJson, new TypeReference<List<UpdateProjectAdminUsecase.UserInProjectRequestDto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse users JSON", e);
        }

        Optional<ProjectEntity> projectOpt = projectDao.findById(projectId);
        if (projectOpt.isPresent()) {
            ProjectEntity project = projectOpt.get();
            project.setName(name);
            project.setDescription(description);

            // 이미지가 빈 값이 아닌 경우에만 업데이트
            if (img != null && !img.isEmpty()) {
                project.setImg(uploadFileToS3(img));
            }

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
    public UpdateUserResponseDto updateUser(String adminId, String userId, String email, String name, String status, MultipartFile profileImg, String projectIdsJson) {
        List<String> projectIds = Collections.emptyList();
        if (projectIdsJson != null && !projectIdsJson.isEmpty() && !"undefined".equals(projectIdsJson)) {
            try {
                projectIds = objectMapper.readValue(projectIdsJson, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse projectIds JSON", e);
            }
        }

        UserAccountStatus userStatus;
        try {
            userStatus = UserAccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status, e);
        }

        Optional<UserEntity> userOpt = userDao.findById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setEmail(email);
            user.setName(name);
            user.setStatus(userStatus);

            // 프로필 이미지가 빈 값이 아닌 경우에만 업데이트
            if (profileImg != null && !profileImg.isEmpty()) {
                user.setProfileImg(uploadFileToS3(profileImg));
            }

            user.setProjectIds(projectIds);

            UserEntity updatedUser = userDao.save(user);
            return new UpdateUserResponseDto(updatedUser);
        } else {
            throw new RuntimeException("User not found.");
        }
    }


    @Override
    public SearchUserAdminResponseDto searchUsers(String adminId, String status, String searchType, String searchText) {
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
            String adminId,
            String name,
            String userId,
            Integer leftChanceForUserstory,
            String startDate,
            String endDate,
            Integer progress,
            int page,
            int pageSize,
            String userName
    ) {
        DateTimeFormatter requestFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        List<ProjectEntity> projects = projectDao.findAll();

        Map<String, UserEntity> userMap = new HashMap<>();
        for (ProjectEntity project : projects) {
            for (ProjectEntity.UserInProjectEntity userInProject : project.getUsers()) {
                userDao.findById(userInProject.getUserId()).ifPresent(userEntity -> {
                    userMap.put(userEntity.getId(), userEntity);
                });
            }
        }

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
        if (userName != null && !userName.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> project.getUsers().stream()
                            .anyMatch(user -> {
                                UserEntity userEntity = userMap.get(user.getUserId());
                                return userEntity != null && userName.equalsIgnoreCase(userEntity.getName());
                            }))
                    .collect(Collectors.toList());
        }

        long totalCount = projects.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, projects.size());
        List<ProjectEntity> paginatedProjects = projects.subList(startIndex, endIndex);

        return new SearchProjectAdminResponseDto(paginatedProjects, totalCount, userMap);
    }

    @Override
    public GetChatgptPriceResponseDto getChatgptPrice(String adminId){
        return new GetChatgptPriceResponseDto("3.2","4.5","10.4","11.2","40.3");
    }

    private String uploadFileToS3(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            Optional<String> optionalFileUrl = s3Port.uploadMultipartFileToS3(file);
            return optionalFileUrl.orElseThrow(() -> new IllegalStateException("Failed to upload file to S3"));
        }
        return "";
    }
}
