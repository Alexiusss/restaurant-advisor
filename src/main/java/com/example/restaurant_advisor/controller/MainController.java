package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String cuisine,
                       Model model) {
        Iterable<Restaurant> restaurants;

        if (filter != null && !filter.isEmpty()) {
            restaurants = restaurantRepository.findByName(filter);
        } else if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineContains(cuisine);
        }
        else {
            restaurants = restaurantRepository.findAll();
        }
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("filter", filter);
        model.addAttribute("cuisine", cuisine);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String name, @RequestParam String cuisine, Map<String, Object> model) {
        Restaurant restaurant = new Restaurant(name, cuisine);

        restaurantRepository.save(restaurant);
        Iterable<Restaurant> restaurants = restaurantRepository.findAll();

        model.put("restaurants", restaurants);
        return "main";
    }

}
