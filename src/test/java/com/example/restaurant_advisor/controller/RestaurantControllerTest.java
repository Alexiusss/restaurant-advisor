package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.util.RestaurantTestUtil.RESTAURANT1;
import static com.example.restaurant_advisor.util.RestaurantTestUtil.RESTAURANT_MATCHER;
import static com.example.restaurant_advisor.util.UserTestUtil.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(ADMIN_MAIL)
//https://stackoverflow.com/a/57069366
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1.id() + "/restaurant"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT1));
    }

    @Test
    void create() throws Exception {
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

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1.id()))
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RESTAURANT1.id()).isPresent());
    }
}
