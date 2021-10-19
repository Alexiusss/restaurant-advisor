package com.example.restaurant_advisor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.restaurant_advisor.UserTestUtil.ADMIN_MAIL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(ADMIN_MAIL)
class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    void addRestaurantToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/restaurants")
                .file("photo", "123".getBytes())
                .param("name", "new restaurant")
                .param("cuisine", "new cuisine")
                .param("menu", "www.new-menu.com")
                .with(csrf());

        perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }
}
