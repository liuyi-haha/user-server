package com.liuyi.user.remote;

import com.liuyi.user.api.RegisterUser200Response;
import com.liuyi.user.api.SearchUser200Response;
import com.liuyi.user.api.UsersApi;
import com.liuyi.user.application.service.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-server")
public class UserController implements UsersApi {
    private final Application application;

    public UserController(Application application) {
        this.application = application;
    }

    @Override
    @PostMapping("/users")
    public ResponseEntity<RegisterUser200Response> registerUser(
            @RequestParam("nickname") String nickname,
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password) {
        return ResponseEntity.ok(application.registerUser(nickname, avatar, phone, password));
    }


    @Override
    @GetMapping("/users")
    public ResponseEntity<SearchUser200Response> searchUser(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(application.searchUser(keyword));
    }
}
