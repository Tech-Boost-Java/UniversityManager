package com.softserve.academy.controller;

import com.softserve.academy.model.Role;
import com.softserve.academy.model.User;
import com.softserve.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Mock userService.usernameExists to return true for "admin" to skip admin creation
        lenient().when(userService.usernameExists("admin")).thenReturn(true);

        // Mock userService.authenticate to return a User for valid credentials
        User adminUser = new User("admin", "admin123", "admin@example.com", Role.ADMIN);
        adminUser.setId(1L);
        lenient().when(userService.authenticate("admin", "admin123")).thenReturn(Optional.of(adminUser));

        // Mock userService.authenticate to return empty for invalid credentials
        lenient().when(userService.authenticate("wronguser", "wrongpass")).thenReturn(Optional.empty());

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void showLoginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void processLogin_withValidCredentials_shouldRedirectToDashboard() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    void processLogin_withInvalidCredentials_shouldRedirectToLoginWithError() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "wronguser")
                .param("password", "wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Invalid username or password"));
    }

    @Test
    void showDashboard_whenAuthenticated_shouldReturnDashboardView() throws Exception {
        // Create a session with authenticated user
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("username", "admin");

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("username", "admin"));
    }

    @Test
    void showDashboard_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        // Create a session without authentication
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void logout_shouldInvalidateSessionAndRedirectToLogin() throws Exception {
        // Create a session with authenticated user
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("username", "admin");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Verify session is invalidated
        assert session.isInvalid();
    }

    @Test
    void home_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
