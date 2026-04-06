package com.liuyi.user.remote;

import com.liuyi.user.api.RegisterUser200Response;
import com.liuyi.user.api.UsersApi;
import com.liuyi.user.application.service.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class UserController implements UsersApi {
    private final Application application;

    public UserController(Application application) {
        this.application = application;
    }

    @Override
    public ResponseEntity<RegisterUser200Response> registerUser(String nickname, MultipartFile avatar, String phone, String password) {
        return ResponseEntity.ok(application.registerUser(nickname, avatar, phone, password));
    }
}
