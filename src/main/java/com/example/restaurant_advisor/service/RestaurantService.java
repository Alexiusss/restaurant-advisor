package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.dto.RestaurantDto;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.restaurant_advisor.util.ControllerUtils.*;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.assureIdConsistent;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkNew;

@Service
@Slf4j
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Cacheable("restaurants")
    public void getRestaurantList(Pageable pageable, String filter, String cuisine, Model model) {

        List<RestaurantDto> restaurants;

        if (filter != null && !filter.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(filter);
        } else if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineOrNameContains(cuisine);
        } else {
            restaurants = restaurantRepository.getAll();
        }
        Page<RestaurantDto> page = createPageFromList(pageable, restaurants);

        model.addAttribute("page", page);
        model.addAttribute("url", "/restaurants");
        model.addAttribute("filter", filter);
        model.addAttribute("cuisine", cuisine);
    }

    public void getRestaurant(int id, Model model, Pageable pageable, AuthUser authUser) {
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
        model.addAttribute("url", "/restaurants/" + id);
        model.addAttribute("rating", getRestaurantRating(reviews));
        model.addAttribute("ratings", ratings);
    }

    public void addRestaurant(Restaurant restaurant, MultipartFile photo) throws IOException {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        restaurant.setFilename(saveFile(photo, uploadPath));
        restaurantRepository.save(restaurant);
    }

    public void updateRestaurant(Restaurant restaurant, MultipartFile photo) throws IOException {

        assureIdConsistent(restaurant, restaurant.id());
        log.info("update restaurant {}", restaurant.id());

        if (!ObjectUtils.isEmpty(photo)) {
            restaurant.setFilename(saveFile(photo, uploadPath));
        }

        restaurantRepository.save(restaurant);
    }
}