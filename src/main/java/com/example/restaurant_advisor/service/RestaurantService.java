package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.model.dto.RestaurantDto;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.restaurant_advisor.util.ControllerUtils.createPageFromList;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Cacheable("restaurants")
    public Page<RestaurantDto> restaurantList(Pageable pageable, String filter, String cuisine) {

        List<RestaurantDto> restaurants;

        if (filter != null && !filter.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(filter);
        } else if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(cuisine);
        } else {
            restaurants = restaurantRepository.getAll();
        }
        return createPageFromList(pageable, restaurants);
    }
}