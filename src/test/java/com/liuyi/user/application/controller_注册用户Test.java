package com.liuyi.user.application;

import com.liuyi.user.adapter.FakeEventBus;
import com.liuyi.user.adapter.FakeFileApiGateway;
import com.liuyi.user.api.RegisterUser200Response;
import com.liuyi.user.api.RegisterUser200ResponseData;
import com.liuyi.user.application.fixture.UserTextFixture;
import com.liuyi.user.application.service.Application;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.liuyi.common.domain.event.Event;
import org.liuyi.file.api.UploadFileRequest;
import org.liuyi.use_api.event.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_注册用户Test {
    @Autowired
    private FakeEventBus eventBus;
    @Autowired
    private FakeFileApiGateway fileService;
    @Autowired
    private Application application;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FakeFileApiGateway fakeFileApiGateway;

    @BeforeEach
    void reset() {
        // 重置所有Fake类的内存
        fileService.reset();
        eventBus.reset();
        fakeFileApiGateway.reset();
    }

    @Test
    void 注册用户时_如果昵称格式不符合要求_错误码应该被正确设置() {
        UserTextFixture.UserDTO user = UserTextFixture.buildNormalUser().nickname("").build();
        MockMultipartFile avatar = new MockMultipartFile(
                "filename",
                "filename",
                MediaType.IMAGE_PNG_VALUE,
                user.getAvatar().getBytes()
        );

        RegisterUser200Response resp = application.registerUser(user.getNickname(), avatar, user.getPhone(), user.getHashedPassword());
        // 验证errCode是否符合预期
        assertEquals(RegisterUser200Response.ErrCodeEnum.NICKNAME_INVALID, resp.getErrCode());
        assertEquals(false, resp.getSuccess());
    }

    @Test
    void 注册用户时_如果用户头像太大_错误信息应该被正确设置() {
        UserTextFixture.UserDTO user = UserTextFixture.buildNormalUser().avatar("a".repeat(10244 * 1024 * 5 + 1)).build();
        MockMultipartFile avatar = new MockMultipartFile(
                "filename",
                "filename",
                MediaType.IMAGE_PNG_VALUE,
                user.getAvatar().getBytes()
        );

        RegisterUser200Response resp = application.registerUser(user.getNickname(), avatar, user.getPhone(), user.getHashedPassword());
        // 验证errCode是否符合预期
        assertEquals(RegisterUser200Response.ErrCodeEnum.UNKNOWN_ERROR, resp.getErrCode());
        // 验证errMsg是否符合预期
        assertEquals("头像体积不能超过5MB", resp.getErrMsg());
        assertEquals(false, resp.getSuccess());

    }

    @Test
    void 注册用户时_如果手机号已经存在_错误信息应该被正确设置() {
        UserTextFixture.UserDTO user = UserTextFixture.buildNormalUser().build();
        MockMultipartFile avatar = new MockMultipartFile(
                "filename",
                "filename",
                MediaType.IMAGE_PNG_VALUE,
                user.getAvatar().getBytes()
        );

        userRepository.save(user.toDomain());


        RegisterUser200Response resp = application.registerUser(user.getNickname(), avatar, user.getPhone(), user.getHashedPassword());
        // 验证errCode是否符合预期
        assertEquals(RegisterUser200Response.ErrCodeEnum.PHONE_REGISTERED, resp.getErrCode());
        assertEquals(false, resp.getSuccess());

    }

    @Test
    void 注册用户时_如果一切正常_上传头像的request应该正确_仓储中应该多出用户信息_发布的事件应该正确_响应应该正确() {
        UserTextFixture.UserDTO user = UserTextFixture.buildNormalUser().build();
        MockMultipartFile avatar = new MockMultipartFile(
                "filename",
                "filename",
                MediaType.IMAGE_PNG_VALUE,
                user.getAvatar().getBytes()
        );

        RegisterUser200Response resp = application.registerUser(user.getNickname(), avatar, user.getPhone(), user.getHashedPassword());

        // 验证resp是否符合预期
        assertNull(resp.getErrCode());
        assertEquals(true, resp.getSuccess());
        RegisterUser200ResponseData data = resp.getData();
        assertNotNull(data);
        assertNotNull(data.getUserId());
        assertEquals(fakeFileApiGateway.getUploadFileResponses().get(0).getFileId(), data.getAvatarFileId());

        // 验证uploadFile接口的request是否符合预期
        UploadFileRequest expectedUploadFileRequest = new UploadFileRequest();
        expectedUploadFileRequest.setContent(user.getAvatar().getBytes());
        UploadFileRequest actualUploadFileRequest = fileService.getUploadFileRequestList().get(0);
        assertThat(actualUploadFileRequest).usingRecursiveComparison().isEqualTo(expectedUploadFileRequest);

        // 验证按照用户ID查出来的User对象是否符合预期
        Assertions.assertNotNull(resp.getData());
        Optional<User> optionalUser = userRepository.findById(resp.getData().getUserId());
        User foundUser = assertThat(optionalUser).isPresent().get().actual();

        User expectedUser = user.toDomain();
        assertNotNull(foundUser.getAvatar().getFileId());
        assertThat(foundUser).usingRecursiveComparison().ignoringFields("avatar", "userId").isEqualTo(expectedUser);

        // 验证发布的事件是否符合预期（使用FakeEventBus）
        Event event = eventBus.getEvent(UserRegisteredEvent.TOPIC).get();
        UserRegisteredEvent expectedEvent = UserRegisteredEvent.builder().userId(resp.getData().getUserId()).phone(user.getPhone()).hashedPassword(user.getHashedPassword()).build();
        assertInstanceOf(UserRegisteredEvent.class, event);
        assertEquals(expectedEvent, event);
    }

}
