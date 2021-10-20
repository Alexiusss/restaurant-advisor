package com.example.restaurant_advisor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.UserTestUtil.ADMIN_MAIL;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(ADMIN_MAIL)
class RootControllerTest extends AbstractControllerTest {

    @Test
    void getRestaurantList() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"));
    }

    @Test
    void getRestaurantListFilteredByName() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants").param("filter", "restaurant"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"))
                .andExpect(model().attribute("filter", "restaurant"));
    }

    @Test
    void getRestaurantListFilteredByCuisine() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants").param("cuisine", "italian"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attribute("url", "/restaurants"))
                .andExpect(model().attribute("cuisine", "italian"));
    }

    @Test
    void getRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/3"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("restaurant"))
                .andExpect(model().attribute("url", "/restaurants/3"));
    }


    @Test
    void getUserList() throws Exception {
        perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("userList"));
    }

    @Test
    void getAllReviews() throws Exception {
        perform(MockMvcRequestBuilders.get("/reviews"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("reviews"))
                .andExpect(model().attribute("url", "/reviews"));
    }

    @Test
    void getUserReviews() throws Exception {
        perform(MockMvcRequestBuilders.get("/user-reviews/1"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("userReviews"))
                .andExpect(model().attribute("url", "/user-reviews/1"));
    }
}