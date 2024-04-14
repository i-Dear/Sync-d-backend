package com.syncd.adapter.out.persistence;

import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.application.domain.user.UserMapper;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.port.out.persistence.user.dto.UserDto;
import com.syncd.application.port.out.persistence.user.dto.UserId;
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
    public UserId createUser(String userName, String email, String password) {
        // UserEntity 생성
        UserEntity newUser = UserEntity.builder()
                .email(email)
                .password(password) // 비밀번호는 보통 저장 전에 해싱 처리를 해야 합니다.
                .name(userName)
                .build();

        // MongoDB에 저장
        UserEntity savedUser = userDao.save(newUser);

        // 저장된 User의 ID 반환
        return new UserId(savedUser.getUserId());
    }
    // ======================================
    // READ
    // ======================================
    @Override
    public UserDto findByEmail(String email){
        return UserMapper.INSTANCE.UserDtoFromEntity(userDao.findByEmail(email).get());
    }

    @Override
    public UserDto findByEmailAndPassword(String email, String password){
        return UserMapper.INSTANCE.UserDtoFromEntity(userDao.findByEmailAndPassword(email,password).get());
    }
}
