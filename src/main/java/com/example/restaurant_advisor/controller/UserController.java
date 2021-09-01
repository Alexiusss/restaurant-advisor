package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.restaurant_advisor.util.ControllerUtils.checkSingleModification;
import static com.example.restaurant_advisor.util.UserUtil.prepareToSave;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList() {
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    @ResponseBody
    public List<User> getAll() {
        return userRepository.findAll();
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void activate(@PathVariable int id, @RequestParam boolean active) {
        userService.activate(id, active);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        checkSingleModification(userRepository.delete(id), "User id= " + id + " missed");
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

    @GetMapping("subscribe/{userId}")
    public String subscribe(@AuthenticationPrincipal AuthUser currentUser,
                            @PathVariable int userId) {
        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        userService.subscribe(currentUser, user);

        return "redirect:/user-reviews/" + user.getId();
    }

    @GetMapping("unsubscribe/{userId}")
    public String unsubscribe(@AuthenticationPrincipal AuthUser currentUser,
                              @PathVariable int userId) {
        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-reviews/" + user.getId();
    }

    @GetMapping("{type}/{userId}/list")
    public String userList(
            Model model,
            @PathVariable int userId,
            @PathVariable String type
    ) {
        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }
}