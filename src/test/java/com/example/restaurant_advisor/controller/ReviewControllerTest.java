package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_advisor.util.RestaurantTestUtil.NOT_FOUND_ID;
import static com.example.restaurant_advisor.util.ReviewTestUtil.*;
import static com.example.restaurant_advisor.util.UserTestUtil.ADMIN_MAIL;
import static com.example.restaurant_advisor.util.UserTestUtil.USER_MAIL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(USER_MAIL)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest extends AbstractControllerTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/reviews/20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REVIEW_MATCHER.contentJson(REVIEW));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/reviews/" + NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void save() throws Exception {
        Review newReview = getNewReview();

        ResultActions action = perform(addNewReview("/reviews/4", newReview))
                .andExpect(authenticated())
                .andExpect(status().isCreated());

        Review created = REVIEW_MATCHER.readFromJson(action);
        int newId = created.getId();
        newReview.setId(newId);
        REVIEW_MATCHER.assertMatch(created, newReview);
        REVIEW_MATCHER.assertMatch(reviewRepository.getExisted(newId), newReview);
    }

    @Test
    void saveInvalid() throws Exception {
        perform(addNewReview("/reviews/5", getInvalidReview()))
                .andExpect(authenticated())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Review updatedReview = reviewRepository.getExisted(REVIEW_ID);
        updatedReview.setTitle("Updated title");
        updatedReview.setComment("Updated comment");
        updatedReview.setActive(true);

        perform(updateReview("/reviews", updatedReview))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        REVIEW_MATCHER.assertMatch(reviewRepository.getExisted(REVIEW_ID), updatedReview);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Review updatedInvalidReview = reviewRepository.getExisted(REVIEW_ID);
        updatedInvalidReview.setTitle("");

        perform(updateReview("/reviews", updatedInvalidReview))
                .andExpect(authenticated())
                //  https://stackoverflow.com/a/57382212
                .andExpect(content().string(containsString("Please fill the title")));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/user-reviews/" + REVIEW_DELETE_ID))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
        assertFalse(reviewRepository.findById(REVIEW_DELETE_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/user-reviews/" + NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void likeAndDislike() throws Exception {
        Review likedReview = reviewRepository.getWithLikesAndUser(REVIEW_ID);

        assertEquals(likedReview.getLikes().size(), 0);

        perform(MockMvcRequestBuilders.get("/reviews/" + REVIEW_ID + "/like"))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        assertEquals(likedReview.getLikes().size(), 1);

        perform(MockMvcRequestBuilders.get("/reviews/" + REVIEW_ID + "/like"))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        assertEquals(likedReview.getLikes().size(), 0);
    }
}