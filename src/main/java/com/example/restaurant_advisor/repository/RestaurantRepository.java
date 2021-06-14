package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByName(String name);

    List<Restaurant> findByCuisineContains(String cuisine);

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "LEFT OUTER JOIN FETCH r.reviews v ")
    List<Restaurant> getAllWithReviews();
}