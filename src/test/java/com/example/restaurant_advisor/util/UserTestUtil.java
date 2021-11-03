package com.example.restaurant_advisor.util;

import com.example.restaurant_advisor.controller.MatcherFactory;
import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@UtilityClass
public class UserTestUtil {

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password", "password2", "activationCode", "reviews");

    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;
    public static final int NOT_FOUND = 1000;

    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String USER_MAIL = "user@yandex.ru";

    public static final User ADMIN = new User(ADMIN_ID, ADMIN_MAIL, "Admin", "AdminLast", "1", true, null, List.of(Role.ADMIN, Role.USER));
    public static final User USER = new User(USER_ID, USER_MAIL, "User", "UserLast", "1", true, null, Collections.singleton(Role.USER));
    public static final List<User> USER_LIST = List.of(ADMIN, USER);

    public static User getNewUser() {
        User newUser = new User(null, "test@mail.ru", "Last", "LastName", "1", false, null, Collections.singleton(Role.USER));
        newUser.setPassword2("1");
        return newUser;
    }

    public static MockHttpServletRequestBuilder addNewUser(String url, User newUser) {
        return multipart(url)
                .param("email", newUser.getEmail())
                .param("firstName", newUser.getFirstName())
                .param("lastName", newUser.getLastName())
                .param("password", newUser.getPassword())
                .param("password2", newUser.getPassword2())
                .param("USER", "")
                .with(csrf());
    }


    public static MockHttpServletRequestBuilder updateUser(String url, User updated) {
        return multipart(url)
                .param("id", updated.getId().toString())
                .param("email", updated.getEmail())
                .param("firstName", updated.getFirstName())
                .param("lastName", updated.getLastName())
                .param("password", updated.getPassword())
                .param("password2", updated.getPassword())
                .param("USER", "")
                .with(csrf());
    }
}