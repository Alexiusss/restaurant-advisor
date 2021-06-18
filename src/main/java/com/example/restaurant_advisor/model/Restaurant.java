package com.example.restaurant_advisor.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"reviews"})
public class Restaurant extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cuisine", nullable = false)
    private String cuisine;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Contact contact;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Set<Review> reviews;

    private String filename;

    public double rating() {
        if (reviews == null) return 0;
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average().orElse(0);
    }

    public Restaurant(String name, String cuisine) {
        super(null);
        this.name = name;
        this.cuisine = cuisine;
    }

    public Restaurant(Integer id, String name, String cuisine) {
        super(id);
        this.name = name;
        this.cuisine = cuisine;
    }
}
