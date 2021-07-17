package com.example.restaurant_advisor;

import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;

import java.util.List;

public class UserTestUtil {

    public static final int ADMIN_ID = 1;
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "Admin", "LastName", "1", true,null, List.of(Role.ADMIN, Role.USER));
}