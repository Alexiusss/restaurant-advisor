package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.example.restaurant_advisor.util.UserTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithUserDetails(ADMIN_MAIL)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminController.REST_URL + "/";

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_MATCHER.contentJson(USER_LIST));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_MATCHER.contentJson(ADMIN));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAccessDenied() throws Exception {
        String content = perform(MockMvcRequestBuilders.get(REST_URL))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("Access is denied"));
    }

    @Test
    void saveAndActivate() throws Exception {
        User newUser = getNewUser();
        newUser.setActive(true);

        ResultActions action = perform(addNewUser(REST_URL, newUser))
                .andExpect(authenticated())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getExisted(newId), newUser);

        perform(MockMvcRequestBuilders.patch(REST_URL + created.getId())
                .param("active", "true"))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        assertTrue(userRepository.getExisted(newId).isActive());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdatedUser();

        perform(updateUser(REST_URL, updated))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(USER_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findByEmailIgnoreCase(USER_MAIL).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, "", "", "", "newPass", false, "", Collections.singleton(Role.USER));
        perform(addNewUser(REST_URL, invalid))
                .andExpect(authenticated())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        User updated = USER;
        updated.setFirstName("");

        perform(updateUser(REST_URL, updated))
                .andExpect(authenticated())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = new User(null, USER_MAIL, "newName", "NewLastName", "newPass", false, "", Collections.singleton(Role.USER));
        expected.setPassword2("newPass");
        String content = perform(addNewUser(REST_URL, expected))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("User with this email already exists"));

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User expected = USER;
        expected.setPassword2("1");
        String content = perform(addNewUser(REST_URL, expected))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("User with this email already exists"));

    }

    @Test
    void activateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + NOT_FOUND)
                .param("active", "true"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void saveWithDifferentPasswords() throws Exception {
        User newUser = getNewUser();
        newUser.setPassword2("diffPass");

        String content = perform(addNewUser(REST_URL, newUser))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(content.contains("Passwords are different"));
    }
}