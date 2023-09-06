package com.example.demo.model;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String username;
    private String hashedPassword;
    private Set<Role> roles;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.roles = new HashSet<>();
    }


    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

