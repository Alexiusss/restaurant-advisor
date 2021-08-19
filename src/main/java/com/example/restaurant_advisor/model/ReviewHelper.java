package com.example.restaurant_advisor.model;

public class ReviewHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getFirstName() + " " + author.getLastName() : "<none>";
    }
}