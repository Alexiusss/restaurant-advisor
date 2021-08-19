package com.example.restaurant_advisor.model.dto;

import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.ReviewHelper;
import com.example.restaurant_advisor.model.User;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class ReviewDto extends BaseTo {

    int rating;
    String title;
    String comment;
    LocalDate date;
    boolean active;
    User author;
    String filename;
    Long likes;
    Boolean meLiked;

    public ReviewDto(Review review, Long likes, Boolean meLiked) {
        super(review.id());
        this.rating = review.getRating();
        this.title = review.getTitle();
        this.comment = review.getComment();
        this.date = review.getDate();
        this.active = review.isActive();
        this.author = review.getUser();
        this.filename = review.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return ReviewHelper.getAuthorName(author);
    }

    @Override
    public String toString() {
        return "ReviewDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}