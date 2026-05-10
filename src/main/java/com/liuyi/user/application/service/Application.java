package com.liuyi.user.application.service;

import com.liuyi.user.api.RegisterUser200Response;
import com.liuyi.user.api.RegisterUser200ResponseData;
import com.liuyi.user.api.SearchUser200Response;
import com.liuyi.user.api.SearchUser200ResponseData;
import com.liuyi.user.application.event.UserRegisteredEvent;
import com.liuyi.user.domain.exception.NicknameInvalidException;
import com.liuyi.user.domain.exception.PhoneRegisteredException;
import com.liuyi.user.domain.exception.UserNotFoundException;
import com.liuyi.user.domain.service.UserService;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.publisher.UserRegisteredEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.use_api.dubbo.get_user.GetUserRequest;
import org.liuyi.use_api.dubbo.get_user.GetUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class Application {
    private final UserService userService;
    private final UserRegisteredEventPublisher userRegisteredEventPublisher;

    public Application(UserService userService, UserRegisteredEventPublisher userRegisteredEventPublisher) {
        this.userService = userService;
        this.userRegisteredEventPublisher = userRegisteredEventPublisher;
    }

    @Transactional
    public RegisterUser200Response registerUser(String nickname, MultipartFile avatar, String phone, String password) {
        try {
            log.info("开始注册用户，nickname={}, phone={}", nickname, phone);
            User user = userService.registerUser(nickname, phone, avatar.getBytes(), password);
            userRegisteredEventPublisher.publish(new UserRegisteredEvent(user.getUserId().value(), phone, password));
            return buildSuccessResponse(user);
        } catch (NicknameInvalidException e) {
            log.error("无效昵称", e);
            return buildFailureResponse(RegisterUser200Response.ErrCodeEnum.NICKNAME_INVALID, e.getMessage());
        } catch (PhoneRegisteredException e) {
            log.warn("手机号已被注册", e);
            return buildFailureResponse(RegisterUser200Response.ErrCodeEnum.PHONE_REGISTERED, e.getMessage());
        } catch (DomainException e) {
            log.warn("领域逻辑错误", e);
            return buildFailureResponse(RegisterUser200Response.ErrCodeEnum.UNKNOWN_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("基础设施层错误", e);
            return buildFailureResponse(RegisterUser200Response.ErrCodeEnum.UNKNOWN_ERROR, e.getMessage());
        }


    }

    private RegisterUser200Response buildSuccessResponse(User user) {
        RegisterUser200Response response = new RegisterUser200Response(true);
        response.setData(new RegisterUser200ResponseData().userId(user.getUserId().value()).avatarFileId(user.getAvatar().getFileId()));
        return response;
    }

    private RegisterUser200Response buildFailureResponse(RegisterUser200Response.ErrCodeEnum errCode, String errMsg) {
        RegisterUser200Response response = new RegisterUser200Response(false);
        response.setErrCode(errCode);
        response.setErrMsg(errMsg);
        return response;
    }

    @Transactional
    public GetUserResponse getUser(GetUserRequest req) {
        try {
            User user = userService.getUser(req.getUserId());
            log.info("成功获取用户信息，userId={}", req.getUserId());
            return GetUserResponse.builder()
                    .success(true)
                    .avatarFileId(user.getAvatar().getFileId())
                    .userId(user.getUserId().value())
                    .phone(user.getPhone().phone())
                    .nickname(user.getNickname().getNickname())
                    .desc(user.getDescription().getDescription())
                    .build();

        } catch (Exception ex) {
            log.error("获取用户信息失败", ex);
            GetUserResponse resp = new GetUserResponse();
            resp.setSuccess(false);
            resp.setErrMsg(ex.getMessage());
            return resp;
        }
    }

    @Transactional
    public SearchUser200Response searchUser(String keyword) {
        try {
            User user = userService.searchUser(keyword);
            SearchUser200Response response = new SearchUser200Response(true);
            SearchUser200ResponseData data = new SearchUser200ResponseData();
            data.setDescription(user.getDescription().getDescription());
            data.setNickname(user.getNickname().getNickname());
            data.setPhone(user.getPhone().phone());
            data.setUserId(user.getUserId().value());
            data.setAvatarFileId(user.getAvatar().getFileId());
            response.setData(data);
            return response;
        } catch (UserNotFoundException ex) {
            log.error("用户不存在", ex);
            SearchUser200Response resp = new SearchUser200Response(false);
            resp.setErrCode(SearchUser200Response.ErrCodeEnum.USER_NOT_FOUND);
            resp.setErrMsg(ex.getMessage());
            return resp;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            SearchUser200Response resp = new SearchUser200Response(false);
            resp.setErrMsg(ex.getMessage());
            return resp;
        }
    }
}

