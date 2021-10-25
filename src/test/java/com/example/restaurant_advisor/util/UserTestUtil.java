package com.example.restaurant_advisor.util;

import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserTestUtil {
    public static final int ADMIN_ID = 1;
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final User ADMIN = new User(ADMIN_ID, ADMIN_MAIL, "Admin", "LastName", "1", true,null, List.of(Role.ADMIN, Role.USER));
    public static final User NEW_USER = new User(null, "test@mail.ru", "Last", "LastName", "1", false,null, List.of(Role.USER));
}