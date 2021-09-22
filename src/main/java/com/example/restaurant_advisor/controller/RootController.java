package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import com.example.restaurant_advisor.repository.ReviewRepository;
import com.example.restaurant_advisor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.example.restaurant_advisor.util.ControllerUtils.createListReviewTos;
import static com.example.restaurant_advisor.util.ControllerUtils.createPageFromList;

@Controller
@Slf4j
public class RootController {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String userList() {
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/reviews")
    public String getAllReviews(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                @AuthenticationPrincipal AuthUser user) {
        log.info("get all reviews");
        List<ReviewDto> reviewsDto =  createListReviewTos(reviewRepository.getAll(), user.getUser());
        model.addAttribute("page", createPageFromList(pageable, reviewsDto));
        model.addAttribute("url", "/reviews");
        return "reviews";
    }

    @GetMapping(value = "/user-reviews/{userId}")
    public String userReviews(@AuthenticationPrincipal AuthUser currentUser,
                              @PathVariable int userId, Model model,
                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("get reviews for user {}", userId);
        userService.getUserReviews(userId, currentUser, model, pageable);
        return "userReviews";
    }
}