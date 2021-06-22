package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main/{id}")
    public String get(@PathVariable int id, Model model) {
        Restaurant restaurant = restaurantRepository.getWithReviewsAndContact(id).orElseThrow();
        model.addAttribute("restaurant", restaurant);
        return "restaurant";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String cuisine,
                       Model model) {
        Iterable<Restaurant> restaurants;

        if (filter != null && !filter.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(filter);
        } else if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(cuisine);
        } else {
            restaurants = restaurantRepository.getAllWithReviews();
        }
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("filter", filter);
        model.addAttribute("cuisine", cuisine);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String name,
                      @RequestParam String cuisine, Map<String, Object> model,
                      @RequestParam("file") MultipartFile file) throws IOException {
        Restaurant restaurant = new Restaurant(name, cuisine);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();

            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            restaurant.setFilename(resultFileName);
        }

        restaurantRepository.save(restaurant);
        Iterable<Restaurant> restaurants = restaurantRepository.getAllWithReviews();

        model.put("restaurants", restaurants);
        return "main";
    }

}
