package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "LEFT OUTER JOIN FETCH r.reviews rr " +
            "LEFT OUTER JOIN FETCH r.contact c " +
            "WHERE r.cuisine LIKE ?1% OR " +
            "r.name LIKE ?1%")
    List<Restaurant> findByCuisineOrNameContains(String filter);

    @EntityGraph(attributePaths = {"reviews", "contact"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> getAllWithReviewsAndContact();

    @EntityGraph(attributePaths = {"reviews", "contact"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithReviewsAndContact(int id);
}