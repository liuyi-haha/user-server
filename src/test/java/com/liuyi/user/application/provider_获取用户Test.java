package com.liuyi.user.application;

import com.liuyi.user.application.fixture.UserTextFixture;
import com.liuyi.user.application.service.Application;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.liuyi.use_api.dubbo.get_user.GetUserRequest;
import org.liuyi.use_api.dubbo.get_user.GetUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class provider_获取用户Test {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Application application;

    @Test
    void 获取用户时_如果用户不存在_应该失败() {
        UserTextFixture.UserDTO userDTO = UserTextFixture.buildNormalUser().userId("nonexistent").build();

        GetUserRequest req = GetUserRequest.builder().userId(userDTO.getUserId()).build();
        GetUserResponse resp = application.getUser(req);
        assertFalse(resp.isSuccess());
    }

    @Test
    void 获取用户时_如果用户存在_应该成功且信息均正确() {
        UserTextFixture.UserDTO userDTO = UserTextFixture.buildNormalUser().build();
        User user = userDTO.toDomain();
        userRepository.save(user);

        GetUserRequest req = GetUserRequest.builder().userId(userDTO.getUserId()).build();
        GetUserResponse resp = application.getUser(req);

        GetUserResponse expectedResp = GetUserResponse.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .avatarFileId(userDTO.getAvatarFileId())
                .phone(userDTO.getPhone())
                .desc(userDTO.getDescription())
                .success(true)
                .build();
        assertThat(resp).usingRecursiveComparison().isEqualTo(expectedResp);
    }

}
