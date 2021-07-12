package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.restaurant_advisor.util.ControllerUtils.getErrors;

@Controller
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping(value = "/user-reviews/{user}")
    public String userReviews(@AuthenticationPrincipal AuthUser currentUser,
                              @PathVariable User user, Model model) {

        List<Review> reviews = reviewRepository.getAllByUserId(user.id());

        model.addAttribute("reviews", reviews);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userReviews";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/reviews")
    public String getAllReviews(Model model, @RequestParam(required = false) Review review) {

        List<Review> reviews = reviewRepository.getAllReviews();

        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewEdit", review);

        return "reviews";
    }

    @PostMapping(value = "/main/{id}/review")
    // https://stackoverflow.com/a/58317766
    public String addReview(@AuthenticationPrincipal AuthUser authUser, @Valid Review review,
                            BindingResult bindingResult, Model model,
                            @PathVariable int id) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("review", review);
        } else {
            review.setDate(LocalDate.now());
            review.setUser(authUser.getUser());
            review.setRestaurant(restaurantRepository.getOne(id));
            model.addAttribute("review", null);
            reviewRepository.save(review);
        }

        Restaurant restaurant = restaurantRepository.getWithReviewsAndContact(id).orElseThrow();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviews", restaurant.getReviews());
        return "restaurant";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/reviews")
    public String updateReview(
            @RequestParam(value = "id") Review review,
            @RequestParam("rating") int rating,
            @RequestParam("title") String title,
            @RequestParam("comment") String comment
    ) throws IOException {

        if (!ObjectUtils.isEmpty(rating)) {
            review.setRating(rating);
        }

        if (!ObjectUtils.isEmpty(title)) {
            review.setTitle(title);
        }

        if (!ObjectUtils.isEmpty(comment)) {
            review.setComment(comment);
        }

        reviewRepository.save(review);

        return "redirect:/reviews";
    }
}