package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Contact;
import com.example.restaurant_advisor.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.util.ContactTestUtil.*;
import static com.example.restaurant_advisor.util.RestaurantTestUtil.*;
import static com.example.restaurant_advisor.util.UserTestUtil.ADMIN_MAIL;
import static com.example.restaurant_advisor.util.UserTestUtil.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(ADMIN_MAIL)
@AutoConfigureMockMvc  (addFilters = false)
class ContactControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ContactController.REST_URL;

    @Autowired
    ContactRepository contactRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(CONTACT_MATCHER.contentJson(CONTACT1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, NOT_FOUND));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAccessDenied() throws Exception {
        String content = perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT1.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("Access is denied"));
    }


    @Test
    void save() throws Exception {
        Contact newContact = new Contact(RESTAURANT2.getId(), "restaurant2 address", "www.restaurant2.com", "restaurant2@email", "22222222", null);

        perform(saveContact(REST_URL, newContact))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(authenticated());
    }

    @Test
    void update() throws Exception {
        Contact updatedContact = new Contact(RESTAURANT3.getId(), "updated address", "www.updated-restaurant2.com", "updated-restaurant2@email", "1234567", null);

        perform(saveContact(REST_URL, updatedContact))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(authenticated());
    }

    @Test
    void createInvalid() throws Exception {
        Contact invalidContact = new Contact(RESTAURANT2.getId(), "", "www.restaurant2.com", "restaurant2@email", "", null);

        perform(saveContact(REST_URL, invalidContact))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(authenticated());
    }
}