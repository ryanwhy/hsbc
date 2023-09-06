package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.TokenInfo;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final Map<String, User> users = new HashMap<>(); // 存储用户信息的映射
    private final Map<String, Role> roles = new HashMap<>(); // 存储角色信息的映射
    private static final Map<String, TokenInfo> tokens = new HashMap<>(); // 存储令牌信息的映射
    private static final long TWO_HOURS_IN_MILLIS = 2 * 60 * 60 * 1000; // 两小时的毫秒数

    public User createUser(String username, String password) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(username, password);
        users.put(username, user);
        return user;
    }

    // 获取单个用户信息
    public User getUser(String userName) {
        // 根据用户名查找用户信息并返回
        return users.get(userName);
    }

    // 获取所有用户信息
    public List<User> getUsers() {
        // 返回所有用户信息的列表
        return new ArrayList<>(users.values());
    }

    public void deleteUser(String username) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("User not found");
        }

        users.remove(username);
    }

    public Role createRole(String roleName) {
        if (isRoleNameTaken(roleName)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        Role role = new Role(roleName);
        roles.put(roleName, role);
        return role;
    }

    public void deleteRole(String roleName) {
        if (!roles.containsKey(roleName)) {
            throw new IllegalArgumentException("Role not found");
        }

        roles.remove(roleName);
    }

    public List<Role> getRoles() {
        // 返回所有用户信息的列表
        return new ArrayList<>(roles.values());
    }

    public void assignRoleToUser(String username, String roleName) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("User not found");
        }
        if (!roles.containsKey(roleName)) {
            throw new IllegalArgumentException("Role not found");
        }

        User user = users.get(username);
        Role role = roles.get(roleName);
        user.addRole(role);
    }

    private boolean isUsernameTaken(String username) {
        return users.values().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private boolean isRoleNameTaken(String roleName) {
        return roles.containsKey(roleName);
    }

    public void invalidateAuthToken(String token) {
        // 从内部映射中移除授权令牌以使其失效
        tokens.remove(token);
    }

    public boolean checkRole(String authToken, String roleName) {
        // 使用授权令牌查找用户
        TokenInfo tokenInfo = tokens.get(authToken);
        if (tokenInfo == null || !isValidToken(authToken)) {
            return false; // 令牌无效
        }

        String username = tokenInfo.getUserName();
        // 根据用户名查找用户
        User user = users.get(username);
        if (user == null) {
            return false; // 用户不存在
        }

        // 检查用户是否具有指定角色
        return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(roleName));
    }

    public Set<String> getAllRoles(String authToken) {
        // 使用授权令牌查找用户
        TokenInfo tokenInfo = tokens.get(authToken);
        if (tokenInfo == null || !isValidToken(authToken)) {
            return null; // 令牌无效
        }

        String username = tokenInfo.getUserName();
        // 根据用户名查找用户
        User user = users.get(username);
        if (user == null) {
            return null; // 用户不存在
        }

        // 获取用户的所有角色
        return user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    private String generateRandomToken() {
        return "random-token-" + System.currentTimeMillis();
    }

    public String generateToken(String userName, String password) {
        // 根据用户名查找用户信息
        User user = getUser(userName);
        if (user != null && user.verifyPassword(password)) {
            String token = generateRandomToken(); // 生成一个随机令牌
            long createTime = System.currentTimeMillis(); // 记录令牌创建时间
            TokenInfo tokenInfo = new TokenInfo(token, userName, createTime);
            tokens.put(token, tokenInfo); // 将令牌信息与令牌关联
            return token;
        } else {
            // 用户名或密码不匹配，返回空令牌或错误提示
            return null;
        }
    }

    public boolean isValidToken(String authToken) {
        TokenInfo tokenInfo = tokens.get(authToken);
        if (tokenInfo != null) {
            long createTime = tokenInfo.getCreateTime();
            long currentTime = System.currentTimeMillis();
            return (currentTime - createTime) <= TWO_HOURS_IN_MILLIS;
        }
        return false;
    }

}
