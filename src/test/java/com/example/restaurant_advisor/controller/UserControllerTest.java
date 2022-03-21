package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.model.dto.CaptchaResponseDto;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;

import static com.example.restaurant_advisor.util.UserTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    private final CaptchaResponseDto response = mock(CaptchaResponseDto.class);

    @Autowired
    MessageSourceAccessor messageSource;

    @Test
    void registration() throws Exception {
        perform(MockMvcRequestBuilders.get("/user/registration"))
                .andExpect(view().name("registration"));
    }

    @Test
    void registerAndActivateNewUser() throws Exception {
        User newUser = getNewUser();
        captchaValidating(restTemplate, response, true);

        // https://memorynotfound.com/integrate-google-recaptcha-spring-web-application-java
        perform(registerNewUser("/user/registration", newUser))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<User> userFromDB = userRepository.findByEmailIgnoreCase(newUser.getEmail());
        assertTrue(userFromDB.isPresent());

        perform(MockMvcRequestBuilders.get("/user/activate/" + userFromDB.get().getActivationCode()))
                .andExpect(model().attribute("messageType","success"))
                .andExpect(model().attribute("message","User successfully activated"))
                .andExpect(view().name("login"));
        assertTrue(userRepository.getExisted(userFromDB.get().id()).isActive());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerInvalid() throws Exception {
        final User invalidUser = getInvalidUser();
        captchaValidating(restTemplate, response, true);

        perform(registerNewUser("/user/registration", invalidUser))
                .andExpect(model().attribute("passwordError", "Passwords are different"))
                .andExpect(model().attribute("password2Error", "Password2 must not be blank"))
                .andExpect(model().attribute("firstNameError", "Firstname must not be blank"))
                .andExpect(model().attribute("emailError", "Email must not be blank"))
                .andExpect(status().isOk());
    }

    @Test
    void registerWithoutCaptcha() throws Exception {
        User newUser = getNewUser();

        captchaValidating(restTemplate, response, false);

        perform(registerNewUser("/user/registration", newUser))
                .andExpect(model().attribute("captchaError", messageSource.getMessage("error.Captcha")))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerDuplicateUser() throws Exception {
        User newUser = getNewUser();
        newUser.setEmail(USER_MAIL);

        captchaValidating(restTemplate, response, true);

        String content = perform(registerNewUser("/user/registration", newUser))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("User with this email already exists"));
    }

    @Test
    void invalidActivation() throws Exception {
        perform(MockMvcRequestBuilders.get("/user/activate/invalid_code"))
                .andExpect(model().attribute("messageType", "danger"))
                .andExpect(model().attribute("message", "Activation code not found!"));

    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getProfile() throws Exception {
        perform(MockMvcRequestBuilders.get("/user/profile"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("email", USER.getEmail()))
                .andExpect(model().attribute("firstName", USER.getFirstName()))
                .andExpect(model().attribute("lastName", USER.getLastName()))
                .andDo(print());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateProfile() throws Exception {
        User updatedUser = getUpdatedUser();

        perform(MockMvcRequestBuilders.post("/user/profile")
                .param("firstName", updatedUser.getFirstName())
                .param("lastName", updatedUser.getLastName())
                .param("password", "")
                .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        USER_MATCHER.assertMatch(updatedUser, userRepository.getExisted(updatedUser.id()));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateAdminProfileRestriction() throws Exception {
        User updatedAdmin = getUpdatedAdmin();

        perform(MockMvcRequestBuilders.post("/user/profile")
                .param("firstName", updatedAdmin.getFirstName())
                .param("lastName", updatedAdmin.getLastName())
                .param("password", "")
                .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError())
                .andExpect(model().attribute("message", "Admin/User modification is forbidden"));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void subscribe() throws Exception {
        perform(MockMvcRequestBuilders.get("/user/subscribe/" + ADMIN_ID)
                .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-reviews/" + ADMIN_ID))
                .andDo(print());

        boolean isSubscribed = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(ADMIN_ID).get().getSubscribers().stream().anyMatch(user -> user.equals(USER));

        assertTrue(isSubscribed);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void unsubscribe() throws Exception {

        perform(MockMvcRequestBuilders.get("/user/unsubscribe/" + USER_ID)
                .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-reviews/" + USER_ID))
                .andDo(print());

        boolean isSubscribed = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(USER_ID).get().getSubscribers().stream().anyMatch(user -> user.equals(ADMIN));

        assertFalse(isSubscribed);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getSubscribers() throws Exception {
        perform(MockMvcRequestBuilders.get("/user/subscribers/" + USER_ID + "/list"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptions"))
                .andExpect(model().attribute("userChannel", USER))
                .andExpect(model().attribute("type", "subscribers"))
                .andExpect(model().attribute("users", Set.of(ADMIN)));
    }
}