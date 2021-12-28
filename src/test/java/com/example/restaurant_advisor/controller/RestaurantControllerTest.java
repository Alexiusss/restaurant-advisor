package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.util.RestaurantTestUtil.*;
import static com.example.restaurant_advisor.util.UserTestUtil.ADMIN_MAIL;
import static com.example.restaurant_advisor.util.UserTestUtil.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID + "/restaurant"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart(REST_URL)
                .file("photo", "123".getBytes())
                .param("name", "new restaurant")
                .param("cuisine", "new cuisine")
                .param("menu", "www.new-menu.com")
                .with(csrf());

        perform(multipart)
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }

    @Test
    void createInvalid() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart(REST_URL)
                .file("photo", "123".getBytes())
                .param("name", "")
                .param("cuisine", "")
                .param("menu", "invalid.com")
                .with(csrf());

        perform(multipart)
                .andExpect(authenticated())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = RESTAURANT2;
        updated.setName("Updated");

        MockHttpServletRequestBuilder multipart = multipart(REST_URL)
                .file("photo", "123".getBytes())
                .param("id", updated.getId().toString())
                .param("name", updated.getName())
                .param("cuisine", updated.getCuisine())
                .param("menu", updated.getMenu())
                .with(csrf());

        perform(multipart)
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(RESTAURANT2.getId()), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant updated = RESTAURANT1;
        updated.setName("");

        MockHttpServletRequestBuilder multipart = multipart(REST_URL)
                .file("photo", "123".getBytes())
                .param("id", updated.getId().toString())
                .param("name", updated.getName())
                .param("cuisine", updated.getCuisine())
                .param("menu", updated.getMenu())
                .with(csrf());

        perform(multipart)
                .andExpect(authenticated())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1.id()))
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RESTAURANT1.id()).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAccessDenied() throws Exception {
        String content = perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1.id() + "/restaurant"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(content.contains("Access is denied"));
    }
}
