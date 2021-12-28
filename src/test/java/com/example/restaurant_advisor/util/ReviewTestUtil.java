package com.example.restaurant_advisor.util;

import com.example.restaurant_advisor.controller.MatcherFactory;
import com.example.restaurant_advisor.model.Review;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

public class ReviewTestUtil{

    public static final MatcherFactory.Matcher<Review> REVIEW_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Review.class,"restaurant", "user", "likes");

    public static final int REVIEW_ID = 20;
    public static final int REVIEW_DELETE_ID = 21;

    public static final Review REVIEW = new Review(REVIEW_ID, 5, "Positive title", "Positive review", LocalDate.now(), null);

    public static Review getNewReview() {
        return new Review(null, 3, "New review", "New Comment", LocalDate.now(), null);
    }

    public static Review getInvalidReview() {
        return new Review(null, 3, "", "Invalid review", LocalDate.now(), null);
    }

    public static MockHttpServletRequestBuilder addNewReview(String url, Review newReview) {
        return multipart(url)
                .file("photo", "".getBytes())
                .param("id", "")
                .param("rating", newReview.getRating().toString())
                .param("title", newReview.getTitle())
                .param("comment", newReview.getComment())
                .with(csrf());
    }

    public static MockHttpServletRequestBuilder updateReview(String url, Review updatedReview) {
        return multipart(url)
                .file("photo", "".getBytes())
                .param("id", updatedReview.getId().toString())
                .param("rating", updatedReview.getRating().toString())
                .param("title", updatedReview.getTitle())
                .param("comment", updatedReview.getComment())
                .param("active", String.valueOf(updatedReview.isActive()))
                .with(csrf());
    }
}