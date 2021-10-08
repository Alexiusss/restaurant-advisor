package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.model.dto.RestaurantDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT DISTINCT NEW com.example.restaurant_advisor.model.dto.RestaurantDto( " +
            "r.id, r.name, r.cuisine, r.filename, " +
            "AVG (rr.rating)" +
            ") " +
            " FROM Restaurant r " +
            "LEFT OUTER JOIN r.reviews rr ON rr.active=true " +
            "WHERE r.cuisine LIKE %:filter% OR " +
            "r.name LIKE %:filter% " +
            "GROUP BY r")
    //  https://www.baeldung.com/spring-jpa-like-queries
    List<RestaurantDto> findByCuisineOrNameContains(@Param("filter") String filter);

    @Query("SELECT DISTINCT NEW com.example.restaurant_advisor.model.dto.RestaurantDto(" +
            "r.id, r.name, r.cuisine, r.filename, " +
            "AVG (rr.rating)" +
            ")" +
            "FROM Restaurant r " +
            "LEFT OUTER JOIN r.reviews rr on rr.active=true " +
            "GROUP BY r.id")
    List<RestaurantDto> getAll();

    @Query("SELECT r FROM Restaurant r " +
            "LEFT OUTER JOIN FETCH r.reviews rr " +
            "LEFT OUTER JOIN FETCH r.contact c " +
            "LEFT OUTER JOIN FETCH rr.user ru " +
            "LEFT OUTER JOIN FETCH rr.likes rl " +
            "WHERE r.id=?1")
    @Cacheable("restaurants")
    Optional<Restaurant> getWithReviewsAndContact(int id);
}