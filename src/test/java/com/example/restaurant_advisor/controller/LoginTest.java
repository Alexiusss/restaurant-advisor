package com.example.restaurant_advisor.controller;


import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.UserTestUtil.ADMIN_MAIL;
import static com.example.restaurant_advisor.UserTestUtil.ADMIN;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginTest extends AbstractControllerTest {

    @Test
    public void contextLoads() throws Exception {
        perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, guest")));
    }

    @Test
    public void loginTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/main"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @Test
    public void correctLoginTest() throws Exception {
        mockMvc.perform(formLogin().user(ADMIN_MAIL).password(ADMIN.getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    public void badCredentials()  throws Exception {
        perform(MockMvcRequestBuilders.post("/login").param("username", "Michael"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}