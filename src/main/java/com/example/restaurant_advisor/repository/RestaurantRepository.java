package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.dto.RestaurantDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT DISTINCT NEW com.example.restaurant_advisor.model.dto.RestaurantDto( " +
            "r, " +
            "AVG (rr.rating)" +
            ") " +
            " FROM Restaurant r " +
            "LEFT OUTER JOIN r.reviews rr ON rr.active=true " +
            "WHERE r.cuisine LIKE ?1% OR " +
            "r.name LIKE ?1% " +
            "GROUP BY r")
    List<RestaurantDto> findByCuisineOrNameContains(String filter);

    @Query("SELECT DISTINCT NEW com.example.restaurant_advisor.model.dto.RestaurantDto( " +
            "r , " +
            "AVG (rr.rating)" +
            ") " +
            "FROM Restaurant r " +
            "LEFT OUTER JOIN r.reviews rr ON rr.active=true " +
            "GROUP BY r ")
    List<RestaurantDto> getAll();

    @Query("SELECT r FROM Restaurant r " +
            "LEFT OUTER JOIN FETCH r.reviews rr " +
            "LEFT OUTER JOIN FETCH r.contact c " +
            "LEFT OUTER JOIN FETCH rr.user ru " +
            "LEFT OUTER JOIN FETCH rr.likes rl " +
            "WHERE r.id=?1")
    Optional<Restaurant> getWithReviewsAndContact(int id);
}