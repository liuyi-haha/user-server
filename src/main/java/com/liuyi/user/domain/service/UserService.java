package com.liuyi.user.domain.service;

import com.liuyi.user.domain.exception.PhoneRegisteredException;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.client.FileClient;
import com.liuyi.user.port.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final FileClient fileClient;
    private final UserRepository userRepository;

    public UserService(FileClient fileClient, UserRepository userRepository) {
        this.fileClient = fileClient;
        this.userRepository = userRepository;
    }

    public User registerUser(String nickname, String phone, byte[] avatar, String password) {
        if (userRepository.existsByPhone(phone)) {
            throw new PhoneRegisteredException("手机号已被注册");
        }
        String fileId = fileClient.uploadAvatar(avatar);
        User user = User.createUser(nickname, phone, fileId, avatar);
        userRepository.save(user);
        return user;
    }
}
