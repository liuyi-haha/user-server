package com.liuyi.user.application;

import com.liuyi.user.api.SearchUser200Response;
import com.liuyi.user.api.SearchUser200ResponseData;
import com.liuyi.user.application.fixture.UserTextFixture;
import com.liuyi.user.application.service.Application;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_搜索用户Test {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Application application;

    @BeforeEach
    void setUp() {
        UserTextFixture.UserDTO userDTO = UserTextFixture.buildNormalUser().build();
        User user = userDTO.toDomain();
        userRepository.save(user);
    }


    @Test
    void 搜索用户时_如果账号既不是有效的用户ID_也不是有效的手机号_应该失败() {
        SearchUser200Response resp = application.searchUser("invalid_user_id_or_phone");
        assertFalse(resp.getSuccess());
        assertEquals(SearchUser200Response.ErrCodeEnum.USER_NOT_FOUND, resp.getErrCode());
    }

    @Test
    void 获取用户时_如果账号是用户ID_应该成功且信息均正确() {
        UserTextFixture.UserDTO userDTO = UserTextFixture.buildNormalUser().build();

        SearchUser200Response expectedResp = new SearchUser200Response();
        SearchUser200Response resp = application.searchUser(userDTO.getUserId());

        expectedResp.setSuccess(true);
        SearchUser200ResponseData data = new SearchUser200ResponseData();
        // 填充data字段
        data.setDescription(userDTO.getDescription());
        data.setNickname(userDTO.getNickname());
        data.setPhone(userDTO.getPhone());
        data.setUserId(userDTO.getUserId());
        data.setAvatarFileId(userDTO.getAvatarFileId());
        expectedResp.setData(data);

        assertThat(resp).usingRecursiveComparison().isEqualTo(expectedResp);
    }

    @Test
    void 获取用户时_如果账号是手机号_应该成功且信息均正确() {
        UserTextFixture.UserDTO userDTO = UserTextFixture.buildNormalUser().build();

        SearchUser200Response expectedResp = new SearchUser200Response();
        SearchUser200Response resp = application.searchUser(userDTO.getPhone());

        expectedResp.setSuccess(true);
        SearchUser200ResponseData data = new SearchUser200ResponseData();
        // 填充data字段
        data.setDescription(userDTO.getDescription());
        data.setNickname(userDTO.getNickname());
        data.setPhone(userDTO.getPhone());
        data.setUserId(userDTO.getUserId());
        data.setAvatarFileId(userDTO.getAvatarFileId());
        expectedResp.setData(data);

        assertThat(resp).usingRecursiveComparison().isEqualTo(expectedResp);
    }
}


