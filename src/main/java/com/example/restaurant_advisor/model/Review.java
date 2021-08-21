package com.example.restaurant_advisor.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"}, name = "reviews_unique_user_restaurant_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"restaurant", "user"})
public class Review extends BaseEntity{

    @Column(name = "rating", nullable = false)
    @NotNull
    @Range(min = 1, max = 5, message = "Please set the rating from 1 to 5")
    private int rating;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Please fill the title")
    @Length(max = 255)
    private String title;

    @Column(name = "comment", length = 2048)
    @NotBlank(message = "Please fill the comment")
    @Length(max = 2048, message = "Comment too long(more than to 2k)")
    private String comment;

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate date;

    @Column(name = "active", nullable = false, columnDefinition = "bool default false")
    private boolean active;

    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "review_likes",
            joinColumns = { @JoinColumn(name = "review_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> likes = new HashSet<>();
}