package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.restaurant_advisor.util.UserUtil.prepareToSave;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal AuthUser authUser) {
        model.addAttribute("email", authUser.getUser().getEmail());
        model.addAttribute("firstName", authUser.getUser().getFirstName());
        model.addAttribute("lastName", authUser.getUser().getLastName());
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password
    ) {
        User user = authUser.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (!ObjectUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(prepareToSave(user));

        return "redirect:/user/profile";
    }
}