package com.liuyi.user.adapter.repository;

import com.liuyi.user.adapter.repository.persistence.data.UserDO;
import com.liuyi.user.adapter.repository.persistence.mapper.UserMapper;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserJpaRepository extends JpaRepository<UserDO, Long> {
    Optional<UserDO> findByUserId(String userId);

    Optional<UserDO> findByPhone(String phone);

    boolean existsByPhone(String phone);
}

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public void save(User user) {
        jpaRepository.save(mapper.toData(user));
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhone(phone);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return jpaRepository.findByPhone(phone).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaRepository.findByUserId(userId).map(mapper::toDomain);
    }
}

