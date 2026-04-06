package com.liuyi.user.port.repository;

import com.liuyi.user.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);

    Optional<User> findById(String userId);
}

