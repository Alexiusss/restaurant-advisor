package com.example.restaurant_advisor.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String userList() {
        return "userList";
    }
}