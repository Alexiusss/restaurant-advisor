package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static com.example.restaurant_advisor.util.ControllerUtils.getErrors;
import static com.example.restaurant_advisor.util.ControllerUtils.saveFile;

@Controller
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ContactRepository contactRepository;

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
        model.addAttribute("reviews", restaurant.getReviews());
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
            restaurants = restaurantRepository.getAllWithReviewsAndContact();
        }
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("filter", filter);
        model.addAttribute("cuisine", cuisine);
        return "main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/main")
    public String add(@Valid Restaurant restaurant, BindingResult bindingResult,
                      Model model, @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("restaurant", restaurant);
        } else {

            restaurant.setFilename(saveFile(file, uploadPath));

            model.addAttribute("restaurant", null);

            restaurantRepository.save(restaurant);
        }

        Iterable<Restaurant> restaurants = restaurantRepository.getAllWithReviewsAndContact();

        model.addAttribute("restaurants", restaurants);
        return "main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/main/{id}")
    public String updateRestaurant(@PathVariable int id,
                                   @RequestParam("name") String name,
                                   @RequestParam("cuisine") String cuisine,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        if (!ObjectUtils.isEmpty(name)) {
            restaurant.setName(name);
        }

        if (!ObjectUtils.isEmpty(cuisine)) {
            restaurant.setCuisine(cuisine);
        }

        if (!ObjectUtils.isEmpty(file)) {
            restaurant.setFilename(saveFile(file, uploadPath));
        }

        restaurantRepository.save(restaurant);

        return "redirect:/main/" + id;
    }
}
