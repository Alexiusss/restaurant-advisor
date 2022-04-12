package com.example.restaurant_advisor.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantDto extends BaseTo {

    String name;
    String cuisine;
    String filename;
    Double rating;

    public RestaurantDto(Integer id, String name, String cuisine, String filename, Double rating) {
        super(id);
        this.name = name;
        this.cuisine = cuisine;
        this.filename = filename;
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