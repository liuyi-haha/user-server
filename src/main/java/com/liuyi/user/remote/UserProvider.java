package com.liuyi.user.remote;


import com.liuyi.user.application.service.Application;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.liuyi.use_api.dubbo.UserService;
import org.liuyi.use_api.dubbo.get_user.GetUserRequest;
import org.liuyi.use_api.dubbo.get_user.GetUserResponse;

@RequiredArgsConstructor
@DubboService
public class UserProvider implements UserService {
    private final Application application;

    @Override
    public GetUserResponse getUser(GetUserRequest getUserRequest) {
        return application.getUser(getUserRequest);
    }

}
