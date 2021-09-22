package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import com.example.restaurant_advisor.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static com.example.restaurant_advisor.util.ControllerUtils.saveFile;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/reviews/{id}")
    public Review getReview(@PathVariable int id) {
        log.info("getById {}", id);
        return reviewRepository.findById(id).orElse(null);
    }

    //  https://stackoverflow.com/a/38733234
    @Transactional
    @GetMapping(value = "/reviews/{reviewId}/like")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void like(
            @AuthenticationPrincipal AuthUser currentUser,
            @PathVariable int reviewId
    ) {
        Review review = reviewRepository.getWithLikesAndUser(reviewId);
        Set<User> likes = review.getLikes();

        if (likes.contains(currentUser.getUser())) {
            log.info("unlike for review {}", reviewId);
            likes.remove(currentUser.getUser());
        } else {
            log.info("like for review {}", reviewId);
            likes.add(currentUser.getUser());
        }
    }

    @PostMapping(value = "/reviews/{id}")
    // https://stackoverflow.com/a/58317766
    @CacheEvict(value = "restaurants", allEntries = true)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addReview(@AuthenticationPrincipal AuthUser authUser, @Valid Review review,
                          @PathVariable int id,
                          @RequestParam(value = "photo") MultipartFile photo) throws IOException {
        log.info("add review {} for restaurant {} from user {}", review, id, authUser.getUser());
        checkNew(review);
        review.setDate(LocalDate.now());
        review.setUser(authUser.getUser());
        review.setRestaurant(restaurantRepository.getExisted(id));
        review.setFilename(saveFile(photo, uploadPath));
        reviewRepository.save(review);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/reviews")
    @CacheEvict(value = "restaurants", allEntries = true)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateReview(
            @RequestParam(value = "id") int id,
            @RequestParam("title") String title,
            @RequestParam("comment") String comment,
            @RequestParam(value = "active") boolean active
    ) {

        log.info("update review {}", id);
        Review review = reviewRepository.getExisted(id);

        if (!ObjectUtils.isEmpty(title)) {
            review.setTitle(title);
        }

        if (!ObjectUtils.isEmpty(comment)) {
            review.setComment(comment);
        }

        review.setActive(active);

        reviewRepository.save(review);
    }

    @DeleteMapping(value = "/user-reviews/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void deleteReview(@PathVariable int id) {
        log.info("delete review {}", id);
        reviewRepository.deleteExisted(id);
    }
}