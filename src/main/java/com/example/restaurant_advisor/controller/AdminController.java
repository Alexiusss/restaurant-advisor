package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import com.example.restaurant_advisor.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "users")
@Tag(name = "Admin Controller")
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Cacheable
    public List<User> getAll() {
        log.info("getAll");
        return Optional.of(userRepository.findAll()).orElse(null);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}")
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
            checkModificationAllowed(user.getId());
            userService.updateUser(user, form);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void activate(@PathVariable int id, @RequestParam boolean active) {
        checkModificationAllowed(id);
        log.info(active ? "active {}" : "dormant {}", id);
        userService.activate(id, active);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void deleteUser(@PathVariable int id) {
        checkModificationAllowed(id);
        log.info("delete {}", id);
        userRepository.deleteExisted(id);
    }
}