package com.example.restaurant_advisor.model.dto;

import com.example.restaurant_advisor.model.Restaurant;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantDto extends BaseTo {

    String name;
    String cuisine;
    String filename;
    Double rating;

    public RestaurantDto(Restaurant restaurant, Double rating) {
        super(restaurant.id());
        this.name = restaurant.getName();
        this.cuisine = restaurant.getCuisine();
        this.filename = restaurant.getFilename();
        this.rating = rating == null ? 0 : rating;
    }

    @Override
    public String toString() {
        return "RestaurantDto{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", filename='" + filename + '\'' +
                ", rating=" + rating +
                '}';
    }
}