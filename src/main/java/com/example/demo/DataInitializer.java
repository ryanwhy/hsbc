package com.example.demo;

import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//this is for demo
@Component
public class DataInitializer {

    private final AuthService authService;

    @Autowired
    public DataInitializer(AuthService authService) {
        this.authService = authService;
        initializeData();
    }

    private void initializeData() {
        // 创建用户
        authService.createUser("user1", "password1");
        authService.createUser("user2", "password2");

        // 创建角色
        authService.createRole("ROLE_ADMIN");
        authService.createRole("ROLE_USER");

        // 分配角色给用户
        authService.assignRoleToUser("user1", "ROLE_ADMIN");
        authService.assignRoleToUser("user2", "ROLE_USER");

        // 创建令牌（用于模拟登录）
        String token1 = authService.generateToken("user1", "password1");
        String token2 = authService.generateToken("user2", "password2");
        System.out.println(token1);
    }
}
