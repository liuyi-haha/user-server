package com.liuyi.user.domain.service;

import com.liuyi.user.domain.exception.PhoneRegisteredException;
import com.liuyi.user.domain.exception.UserNotFoundException;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.client.FileClient;
import com.liuyi.user.port.repository.UserRepository;
import org.liuyi.common.domain.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new PhoneRegisteredException();
        }
        String fileId = fileClient.uploadAvatar(avatar);
        User user = User.createUser(nickname, phone, fileId, avatar);
        userRepository.save(user);
        return user;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DomainException("用户不存在"));
    }

    public User searchUser(String keyword) {
        Optional<User> optionalUser = userRepository.findById(keyword);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        optionalUser = userRepository.findByPhone(keyword);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException();
    }
}
