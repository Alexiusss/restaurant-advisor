package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.dto.RestaurantDto;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import com.example.restaurant_advisor.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.restaurant_advisor.util.ControllerUtils.*;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkNew;

@Controller
@Slf4j
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

        log.info("get restaurant {}", id);
        Restaurant restaurant = restaurantRepository.getWithReviewsAndContact(id).orElseThrow();

        List<ReviewDto> reviews = null;
        Page<ReviewDto> page = null;
        Map<Integer, Integer> ratings = new HashMap<>();
        if (restaurant.getReviews() != null) {
            reviews = createListReviewTos(restaurant.getReviews(), authUser.getUser())
                    .stream()
                    .filter(ReviewDto::isActive)
                    .collect(Collectors.toList());
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
        model.addAttribute("rating", getRestaurantRating(reviews));
        model.addAttribute("ratings", ratings);
        return "restaurant";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String cuisine,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("get restaurant list");
        Page<RestaurantDto> page = restaurantService.restaurantList(pageable, filter, cuisine);

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
            log.info("create restaurant {}", restaurant);
            checkNew(restaurant);
            restaurant.setFilename(saveFile(file, uploadPath));
            model.addAttribute("restaurant", null);
            restaurantRepository.save(restaurant);
        }
        return "redirect:/main/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/main/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/main/{id}")
    public String updateRestaurant(@PathVariable int id,
                                   @RequestParam("name") String name,
                                   @RequestParam("cuisine") String cuisine,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        log.info("update restaurant {}", id);
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
