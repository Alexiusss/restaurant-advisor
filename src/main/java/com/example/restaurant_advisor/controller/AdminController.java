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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkModificationAllowed;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkPasswords;

@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "users")
@Tag(name = "Admin Controller")
public class AdminController {

    public static final String REST_URL = "/admin/users";

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
    public ResponseEntity<User> getUser(@PathVariable int id) {
        log.info("getById {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    public ResponseEntity<User> saveOrUpdate(@Valid User user, @RequestParam Map<String, String> form) {
        checkPasswords(user);
        if (user.isNew()) {
            User created = userService.addUser(user, form);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(created);
        } else {
            checkModificationAllowed(user.id());
            userService.updateUser(user, form);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
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