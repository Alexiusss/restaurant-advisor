package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.model.dto.CaptchaResponseDto;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static com.example.restaurant_advisor.util.ControllerUtils.getErrors;

@Controller
@RequestMapping("user/")
@Slf4j
@CacheConfig(cacheNames = "users")
public class UserController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user, BindingResult bindingResult, Model model) {

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
       CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

       if (!response.isSuccess()){
           model.addAttribute("captchaError", "Fill captcha");
       }

        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "Passwords are different");
        }

        if (bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = getErrors(bindingResult);

            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("activate/{code}")
    @CacheEvict(allEntries = true)
    public String activate(Model model, @PathVariable String code) {
        log.info("user activation with code {}", code);
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code not found!");
        }

        return "login";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get profile for user {}", authUser.getUser());
        model.addAttribute("email", authUser.getUser().getEmail());
        model.addAttribute("firstName", authUser.getUser().getFirstName());
        model.addAttribute("lastName", authUser.getUser().getLastName());
        return "profile";
    }

    @PostMapping("profile")
    @CacheEvict(allEntries = true)
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

        log.info("update profile for user {}", user);
        userService.prepareAndSave(user);
        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{userId}")
    public String subscribe(@AuthenticationPrincipal AuthUser currentUser,
                            @PathVariable int userId) {
        userService.subscribe(currentUser, userId);
        return "redirect:/user-reviews/" + userId;
    }

    @GetMapping("unsubscribe/{userId}")
    public String unsubscribe(@AuthenticationPrincipal AuthUser currentUser,
                              @PathVariable int userId) {
        userService.unsubscribe(currentUser, userId);
        return "redirect:/user-reviews/" + userId;
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
            log.info("get subscriptions list for user {}", userId);
            model.addAttribute("users", user.getSubscriptions());
        } else {
            log.info("get subscribers list for user {}", userId);
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }
}