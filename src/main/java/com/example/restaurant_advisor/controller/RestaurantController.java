package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import com.example.restaurant_advisor.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@CacheConfig(cacheNames = "restaurants")
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant Controller")
public class RestaurantController {

    static final String REST_URL = "/restaurants";

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RestaurantService restaurantService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}/restaurant")
    public Restaurant getRestaurant(@PathVariable int id) {
        log.info("get restaurant by id {}", id);
        return restaurantRepository.getExisted(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @CacheEvict(allEntries = true)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveOrUpdateRestaurant(@Valid Restaurant restaurant, @RequestParam("photo") MultipartFile photo) throws IOException {
        if (restaurant.isNew()) {
            restaurantService.addRestaurant(restaurant, photo);
        } else {
            restaurantService.updateRestaurant(restaurant, photo);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void deleteRestaurant(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        restaurantRepository.deleteExisted(id);
    }
}
