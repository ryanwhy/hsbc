package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthService authService;


    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }
    private List<User> users = new ArrayList<>(); // 用于存储用户数据的列表

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        // 检查用户是否已存在
        authService.createUser(username,password);
        return ResponseEntity.ok("User created successfully");
    }


    // 获取用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        // 处理获取用户信息的逻辑，从内存或其他数据源中获取用户信息
        // 返回用户信息
        Optional<User> optionalUser = Optional.ofNullable(authService.getUser(username));

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取所有用户信息
    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return authService.getUsers();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {

        authService.deleteUser(username);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/role")
    public ResponseEntity<String> createRole(@RequestParam String roleName) {
        // 调用 authService 创建角色的方法
        authService.createRole(roleName);
        return ResponseEntity.ok("Role created successfully");
    }

    @DeleteMapping("/role/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        // 调用 authService 删除角色的方法
        authService.deleteRole(roleName);
        return ResponseEntity.ok("Role deleted successfully");
    }

    @PutMapping("/user/{username}/role/{roleName}")
    public ResponseEntity<String> addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        // 调用 authService 将角色分配给用户的方法
        authService.assignRoleToUser(username, roleName);
        return ResponseEntity.ok("Role added to user successfully");
    }

    @GetMapping("/roles")
    public Collection<Role> getAllRoles() {
        return authService.getRoles();
    }


    @GetMapping("/roles-by-token")
    public ResponseEntity<Collection<String>> getAllRolesByToken(@RequestParam String authToken) {
        Set<String> roles = authService.getAllRoles(authToken);
        if (roles != null) {
            return ResponseEntity.ok(roles);
        } else {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
    @PostMapping("/token")
    public ResponseEntity<String> authenticate(@RequestParam String userName, @RequestParam String password) {
        // 调用 authService 创建令牌的方法
        String token = authService.generateToken(userName,password);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @DeleteMapping("/token/{token}")
    public ResponseEntity<String> invalidateToken(@PathVariable String token) {
        authService.invalidateAuthToken(token);
        return ResponseEntity.ok("Token invalidated successfully");
    }

    @GetMapping("/check-role")
    public ResponseEntity<String> checkRole(@RequestParam String authToken, @RequestParam String roleName) {
        boolean hasRole = authService.checkRole(authToken, roleName);
        if (hasRole) {
            return ResponseEntity.ok("User has the specified role");
        } else {
            return ResponseEntity.ok("User does not have the specified role");
        }
    }

    @GetMapping("/user-roles")
    public ResponseEntity<Collection<String>> getUserRoles(@RequestParam String authToken) {
        Set<String> roles = authService.getAllRoles(authToken);
        if (roles != null) {
            return ResponseEntity.ok(roles);
        } else {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}
