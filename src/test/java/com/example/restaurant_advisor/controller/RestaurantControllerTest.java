package com.example.restaurant_advisor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.UserTestUtil.ADMIN_MAIL;
import static com.example.restaurant_advisor.UserTestUtil.admin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@WithUserDetails(ADMIN_MAIL)
class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    void mainPageTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").string(admin.getFirstName()));
    }

    @Test
    void restaurantListTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='restaurant-list']/div").nodeCount(4));
    }

    @Test
    void filterRestaurantTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/main").param("filter", "restaurant"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='restaurant-list']/div").nodeCount(2))
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=1]").exists())
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=2]").exists());
    }

    @Test
    void cuisineRestaurantTest() throws Exception {
        perform(MockMvcRequestBuilders.get("/main").param("cuisine", "italian"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='restaurant-list']/div").nodeCount(2))
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=3]").exists())
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=4]").exists());
    }

    @Test
    void addRestaurantToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "123".getBytes())
                .param("name", "new restaurant")
                .param("cuisine", "new cuisine")
                .with(csrf());

        perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='restaurant-list']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=33]").exists())
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=33]/div/span").string("new restaurant"))
                .andExpect(xpath("//div[@id='restaurant-list']/div[@data-id=33]/div/i").string("new cuisine"));

    }
}