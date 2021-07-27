package com.example.restaurant_advisor.model;

import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"contact", "reviews"})
public class Restaurant extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Please fill the name")
    @Length(max = 128, message = "Message too long (more than 128)")
    private String name;

    @Column(name = "cuisine", nullable = false)
    @NotBlank(message = "Please fill the cuisine")
    @Length(max = 255, message = "Message too long (more than 255)")
    private String cuisine;

    // https://stackoverflow.com/a/54730020
    @OneToOne(mappedBy = "restaurant", cascade={CascadeType.PERSIST,CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    // https://vladmihalcea.com/hibernate-lazytoone-annotation/
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @PrimaryKeyJoinColumn
    private Contact contact;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant",
            cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Review> reviews;

    private String filename;

    public double rating() {
        if (reviews == null) return 0;
        return reviews.stream()
                .filter(Review::isActive)
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
