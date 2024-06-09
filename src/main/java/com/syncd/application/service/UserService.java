package com.syncd.application.service;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase;
import com.syncd.application.port.in.GetUserInfoUsecase;
//import com.syncd.application.port.in.RegitsterUserUsecase;
import com.syncd.application.port.in.UpdateUserInfoUsecase;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.domain.project.Project;
import com.syncd.domain.project.UserInProject;
import com.syncd.domain.user.User;
//import com.syncd.dto.TokenDto;
//import com.syncd.dto.UserForTokenDto;
import com.syncd.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class UserService implements GetUserInfoUsecase, UpdateUserInfoUsecase {
    private final ReadUserPort readUserPort;
    private final ReadProjectPort readProjectPort;
    private final WriteUserPort writeUserPort;
    private final S3Port s3Port;

    @Override
    public GetUserInfoResponseDto getUserInfo(String userId) {
        User user = readUserPort.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        // GetAllRoomsByUserIdResponseDto responseDto = projectMappers.mapProjectsToGetAllRoomsByUserIdResponseDto(userId, projects);
        GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto allProjects = mapProjectsToResponse(userId, projects);


//        GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto projects = getAllRoomsByUserIdUsecase.getAllRoomsByUserId(userId);
        System.out.println(user);
        return new GetUserInfoResponseDto(userId,hash(userId) ,user.getName(), user.getProfileImg(), user.getEmail(), allProjects.projects());

    }

    private static String hash(String input) {
        try {
            // SHA-256 해시 인스턴스 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // 해시 결과를 16바이트로 자르기
            byte[] truncatedHash = new byte[8];
            System.arraycopy(hashBytes, 0, truncatedHash, 0, 8);

            // 바이트 배열을 헥사 문자열로 변환
            char[] hexChars = Hex.encode(truncatedHash);
            return new String(hexChars);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    @Override
    public UpdateUserInfoResponseDto updateUserInfo(String userId, String name, MultipartFile img) {
        User user = readUserPort.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        String imgURL = "";
        if (img != null && !img.isEmpty()) {
            Optional<String> optionalImgURL = s3Port.uploadMultipartFileToS3(img);
            imgURL = optionalImgURL.orElseThrow(() -> new IllegalStateException("Failed to upload image to S3"));
            user.setProfileImg(imgURL);
        }

        writeUserPort.updateUser(user);

        return new UpdateUserInfoResponseDto(userId, user.getName(), user.getProfileImg());
    }

    private GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto mapProjectsToResponse(String userId, List<Project> projects) {
        List<GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectDtos = projects.stream()
                .map(project -> convertProjectToDto(userId, project))
                .filter(dto -> dto != null)  // Ensure that only relevant projects are included
                .collect(Collectors.toList());

        return new GetAllRoomsByUserIdUsecase.GetAllRoomsByUserIdResponseDto(userId, projectDtos);
    }

    private GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto convertProjectToDto(String userEmail, Project project) {
        Role userRole = project.getUsers().stream()
                .filter(user -> userEmail.equals(user.getUserId()))
                .map(UserInProject::getRole)
                .findFirst()
                .orElse(null);

        if (userRole == null) return null;

        List<UserInProject> usersInProject = project.getUsers();

        List<String> userEmails = usersInProject.stream()
                .map(UserInProject::getUserId)
                .map(userId -> {
                    User user = readUserPort.findByUserId(userId);
                    return user != null ? user.getEmail() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new GetAllRoomsByUserIdUsecase.ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                project.getName(),
                project.getId(),
                project.getDescription(),
                userRole,
                userEmails,
                project.getProgress(),
                project.getLastModifiedDate(),
                project.getImg()
        );
    }
}
