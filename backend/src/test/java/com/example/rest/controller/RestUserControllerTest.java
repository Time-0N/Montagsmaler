package com.example.rest.controller;

import com.example.rest.generated.model.UserUpdateRequest;
import com.example.rest.generated.model.UserUpdateResponse;
import com.example.service.UserService;
import com.example.config.MockSecurityConfig;
import com.example.config.TestWebConfig;
import com.example.data.model.entity.User;
import com.example.security.CustomJwtConverter;
import com.example.security.UserCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestUserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomJwtConverter.class))
@Import({TestWebConfig.class, MockSecurityConfig.class})
@ActiveProfiles("test")
class RestUserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserService userService;
    @MockBean private UserCacheService userCacheService;

    private final UUID testUserId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Test
    void getMe_ReturnsUserDetails() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_ValidRequest_ReturnsUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("newusername", "new@example.com", "New", "User", "Test");
        UserUpdateResponse response = new UserUpdateResponse("newusername", "new@example.com", "New", "User", "Test");

        Mockito.when(userService.updateUser(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/user/update/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newusername"));
    }

    @Test
    void deleteUser_ValidRequest_DeletesUser() throws Exception {
        mockMvc.perform(delete("/api/user/delete/User"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(testUserId);
        verify(userCacheService).evict("kc123");
    }

    @Test
    void getAllUsers_AdminAccess_ReturnsUserList() throws Exception {
        User admin = new User();
        admin.setId(testUserId);
        admin.setUsername("testuser");
        admin.setEmail("test@example.com");
        admin.setKeycloakId("kc123");

        Mockito.when(userService.findAllUsers()).thenReturn(List.of(admin));

        mockMvc.perform(get("/api/user/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }
}
