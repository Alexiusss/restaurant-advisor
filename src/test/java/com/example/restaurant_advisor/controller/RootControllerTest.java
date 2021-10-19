package com.example.restaurant_advisor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.UserTestUtil.ADMIN_MAIL;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(ADMIN_MAIL)
class RootControllerTest extends AbstractControllerTest {

    @Test
    void restaurantListTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"));
    }

    @Test
    void filterRestaurantTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants").param("filter", "restaurant"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"))
                .andExpect(model().attribute("filter", "restaurant"));
    }

    @Test
    void cuisineRestaurantTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants").param("cuisine", "italian"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"))
                .andExpect(model().attribute("cuisine", "italian"));
    }
}