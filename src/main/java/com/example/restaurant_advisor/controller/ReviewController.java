package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;

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