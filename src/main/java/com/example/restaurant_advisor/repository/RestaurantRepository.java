package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "LEFT OUTER JOIN FETCH r.reviews rr " +
            "LEFT OUTER JOIN FETCH r.contact c " +
            "WHERE r.id=:id")
    Optional<Restaurant> getWithReviewsAndContact(@Param("id") int id);
}