package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByName(String name);

    List<Restaurant> findByCuisineContains(String cuisine);
}