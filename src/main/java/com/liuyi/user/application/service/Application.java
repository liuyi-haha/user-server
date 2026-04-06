package com.liuyi.user.application.service;

import com.liuyi.user.api.RegisterUser200Response;
import com.liuyi.user.api.RegisterUser200ResponseData;
import com.liuyi.user.application.event.UserRegisteredEvent;
import com.liuyi.user.domain.exception.NicknameInvalidException;
import com.liuyi.user.domain.exception.PhoneRegisteredException;
import com.liuyi.user.domain.service.UserService;
import com.liuyi.user.domain.user.User;
import com.liuyi.user.port.publisher.UserRegisteredEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.liuyi.common.domain.exception.DomainException;
import org.springframework.stereotype.Service;
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

    public RegisterUser200Response registerUser(String nickname, MultipartFile avatar, String phone, String password) {
        try {
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
        response.setData(new RegisterUser200ResponseData().userId(user.getUserId().value()));
        return response;
    }

    private RegisterUser200Response buildFailureResponse(RegisterUser200Response.ErrCodeEnum errCode, String errMsg) {
        RegisterUser200Response response = new RegisterUser200Response(false);
        response.setErrCode(errCode);
        response.setErrMsg(errMsg);
        return response;
    }
}

