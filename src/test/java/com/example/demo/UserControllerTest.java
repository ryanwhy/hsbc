package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;
    private AuthService authService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        authService = mock(AuthService.class);
        userController = new UserController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        when(authService.createUser("testUser", "testPassword")).thenReturn(new User("testUser", "testPassword"));

        ResultActions resultActions = mockMvc.perform(post("/api/auth/user")
                .param("username", "testUser")
                .param("password", "testPassword")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(authService.getUsers()).thenReturn(Collections.singletonList(new User("user1", "password1")));

        ResultActions resultActions = mockMvc.perform(get("/api/auth/users"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(authService).deleteUser("testUser");

        ResultActions resultActions = mockMvc.perform(delete("/api/auth/user/testUser"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRole() throws Exception {
        when(authService.createRole("testRole")).thenReturn(new Role("testRole"));

        ResultActions resultActions = mockMvc.perform(post("/api/auth/role")
                .param("roleName", "testRole")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Role created successfully"));
    }

    @Test
    public void testDeleteRole() throws Exception {
        doNothing().when(authService).deleteRole("testRole");

        ResultActions resultActions = mockMvc.perform(delete("/api/auth/role/testRole"));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Role deleted successfully"));
    }

    @Test
    public void testAddRoleToUser() throws Exception {
        doNothing().when(authService).assignRoleToUser("testUser", "testRole");

        ResultActions resultActions = mockMvc.perform(put("/api/auth/user/testUser/role/testRole"));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Role added to user successfully"));
    }

    @Test
    public void testGetAllRoles() throws Exception {
        when(authService.getRoles()).thenReturn(Collections.singletonList(new Role("role1")));

        ResultActions resultActions = mockMvc.perform(get("/api/auth/roles"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("role1"));
    }

    @Test
    public void testGetAllRolesByToken() throws Exception {
        when(authService.getAllRoles("testToken")).thenReturn(Collections.singleton("role1"));

        ResultActions resultActions = mockMvc.perform(get("/api/auth/roles-by-token")
                .param("authToken", "testToken"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("role1"));
    }
}
