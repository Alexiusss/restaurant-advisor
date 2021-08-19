package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import com.example.restaurant_advisor.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.restaurant_advisor.util.ControllerUtils.*;

@Controller
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RestaurantService restaurantService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main/{id}")
    public String get(@PathVariable int id, Model model,
                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                      @AuthenticationPrincipal AuthUser authUser) {

        Restaurant restaurant = restaurantRepository.getWithReviewsAndContact(id).orElseThrow();

        List<ReviewDto> reviews;
        Page<ReviewDto> page = null;
        Map<Integer, Integer> ratings = new HashMap<>();
        if (restaurant.getReviews() != null) {
            reviews = createListReviewTos(restaurant.getReviews(), authUser.getUser());
            // https://stackoverflow.com/a/51541841
            reviews.forEach(s -> ratings.merge(s.getRating(), 1, Math::addExact));
            page = createPageFromList(pageable, reviews);
        }
        for (int i = 0; i <= 5; i++) {
            ratings.putIfAbsent(i, 0);
        }

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main/" + id);
        model.addAttribute("ratings", ratings);
        return "restaurant";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String cuisine,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Restaurant> page = restaurantService.restaurantList(pageable, filter, cuisine);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
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
