package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.TokenInfo;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private TokenInfo tokenInfo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() {
        User user = authService.createUser("testUser", "testPassword");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertTrue(user.verifyPassword("testPassword"));
    }

    @Test
    public void testGetUser() {
        User user = authService.createUser("testUser", "testPassword");
        User retrievedUser = authService.getUser("testUser");
        assertNotNull(retrievedUser);
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertTrue(retrievedUser.verifyPassword("testPassword"));
    }

    @Test
    public void testGetUsers() {
        List<User> users = authService.getUsers();
        assertNotNull(users);
    }

    @Test
    public void testDeleteUser() {
        authService.createUser("testUser", "testPassword");
        authService.deleteUser("testUser");
        assertNull(authService.getUser("testUser"));
    }

    @Test
    public void testCreateRole() {
        Role role = authService.createRole("testRole");
        assertNotNull(role);
        assertEquals("testRole", role.getRoleName());
    }

    @Test
    public void testGetRoles() {
        List<Role> roles = authService.getRoles();
        assertNotNull(roles);
    }

    @Test
    public void testDeleteRole() {
        authService.createRole("testRole");
        authService.deleteRole("testRole");
        assertNull(authService.getRoles().stream().filter(r -> r.getRoleName().equals("testRole")).findFirst().orElse(null));
    }

    @Test
    public void testAssignRoleToUser() {
        User user = authService.createUser("testUser", "testPassword");
        Role role = authService.createRole("testRole");
        authService.assignRoleToUser("testUser", "testRole");
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    public void testInvalidateAuthToken() {
        String token = authService.generateToken("testUser", "testPassword");
        authService.invalidateAuthToken(token);
        assertFalse(authService.isValidToken(token));
    }

    @Test
    public void testGenerateTokenValid() {
        User user = authService.createUser("testUser", "testPassword");
        String token = authService.generateToken("testUser", "testPassword");
        assertNotNull(token);
        assertTrue(authService.isValidToken(token));
    }

    @Test
    public void testGenerateTokenInvalidCredentials() {
        authService.createUser("testUser", "testPassword");
        String token = authService.generateToken("testUser", "wrongPassword");
        assertNull(token);
    }

    @Test
    public void testCheckRole() {
        User user = authService.createUser("testUser", "testPassword");
        Role role = authService.createRole("testRole");
        authService.assignRoleToUser("testUser", "testRole");
        String token = authService.generateToken("testUser", "testPassword");

        assertTrue(authService.checkRole(token, "testRole"));
        assertFalse(authService.checkRole(token, "otherRole"));
    }

    @Test
    public void testGetAllRoles() {
        User user = authService.createUser("testUser", "testPassword");
        Role role1 = authService.createRole("testRole1");
        Role role2 = authService.createRole("testRole2");
        authService.assignRoleToUser("testUser", "testRole1");
        authService.assignRoleToUser("testUser", "testRole2");
        String token = authService.generateToken("testUser", "testPassword");

        Set<String> roles = authService.getAllRoles(token);
        assertNotNull(roles);
        assertTrue(roles.contains("testRole1"));
        assertTrue(roles.contains("testRole2"));
    }

}
