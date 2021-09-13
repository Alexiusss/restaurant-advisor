package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @ResponseBody
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public User getUser(@PathVariable int id) {
        log.info("getById {}", id);
        return userRepository.findById(id).orElse(null);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void userSaveOrUpdate(@Valid User user, @RequestParam Map<String, String> form) {
        if (user.isNew()) {
            userService.addUser(user);
        } else {
            userService.updateUser(user, form);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void activate(@PathVariable int id, @RequestParam boolean active) {
        log.info(active ? "active {}" : "dormant {}", id);
        userService.activate(id, active);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        log.info("delete {}", id);
        userRepository.deleteExisted(id);
    }
}