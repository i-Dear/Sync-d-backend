package com.syncd.adapter.out.persistence;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.domain.user.User;
import com.syncd.domain.user.UserMapper;
import com.syncd.dto.UserDto;
import com.syncd.dto.UserId;
import com.syncd.enums.UserAccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        newUser.setEmail(email);
        newUser.setProfileImg(img);
        newUser.setStatus(UserAccountStatus.AVAILABLE);
        // MongoDB에 저장
        UserEntity savedUser = userDao.save(newUser);

        // 저장된 User의 ID 반환
        return new UserId(savedUser.getId());
    }
    // ======================================
    // READ
    // ======================================
    @Override
    public User findByEmail(String email){
        return UserMapper.INSTANCE.fromEntity(userDao.findByEmail(email).get());
    }

    @Override
    public User findByUsername(String username) {
        return UserMapper.INSTANCE.fromEntity(userDao.findByName(username).get());
    }

    @Override
    public User findByUserId(String userId) {
        return UserMapper.INSTANCE.fromEntity(userDao.findById(userId).get());
    }

    @Override
    public boolean isExistUser(String email) {
        return !userDao.findByEmail(email).isEmpty();
    }

}
