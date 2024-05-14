package com.syncd.adapter.out.persistence;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import com.syncd.domain.user.UserMapper;
import com.syncd.dto.UserId;
import com.syncd.enums.UserAccountStatus;
import com.syncd.exceptions.CustomException;
import com.syncd.exceptions.ErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements WriteUserPort, ReadUserPort {

    private final UserDao userDao;

    // ======================================
    // WRITE
    // ======================================
    @Override
    public UserId createUser(String userName, String email,String img) {
        // UserEntity 생성
        UserEntity newUser = new UserEntity();
        newUser.setName(userName);
        newUser.setId(email);
        newUser.setEmail(email);
        newUser.setProfileImg(img);
        newUser.setStatus(UserAccountStatus.AVAILABLE);
        // MongoDB에 저장
        UserEntity savedUser = userDao.save(newUser);
        // 저장된 User의 ID 반환
        return new UserId(savedUser.getId());
    }

    @Override
    public UserId updateUser(User user) {
        // 사용자 정보를 가져옴
        UserEntity existingUser = userDao.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorInfo.USER_NOT_FOUND, "User ID: " + user.getId()));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setProfileImg(user.getProfileImg());

        UserEntity updatedUser = userDao.save(existingUser);

        return new UserId(updatedUser.getId());
    }

    // ======================================
    // READ
    // ======================================
    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email)
                .map(UserMapper.INSTANCE::fromEntity)
                .orElseThrow(() ->  new CustomException(ErrorInfo.USER_NOT_FOUND, "User Email: " + email));
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByName(username)
                .map(UserMapper.INSTANCE::fromEntity)
                .orElseThrow(() -> new CustomException(ErrorInfo.USER_NOT_FOUND, "User Name: " + username));
    }

    @Override
    public User findByUserId(String userId) {
        return userDao.findById(userId)
                .map(UserMapper.INSTANCE::fromEntity)
                .orElseThrow(() -> new CustomException(ErrorInfo.USER_NOT_FOUND, "User ID: " + userId));
    }

    @Override
    public boolean isExistUser(String email) {
        return !userDao.findByEmail(email).isEmpty();
    }

    @Override
    public List<User> usersFromEmails(List<String> emails) {
        return emails.stream().map(el->(
                findByEmail(el)
                ))
                .toList();
    }

}
