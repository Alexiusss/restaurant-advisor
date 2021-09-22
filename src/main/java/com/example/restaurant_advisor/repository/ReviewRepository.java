package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(readOnly = true)
public interface ReviewRepository extends BaseRepository<Review> {

    @Override
    @Modifying
    @Transactional
    Review save(Review review);

        //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"user", "likes"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Review r " +
            "ORDER BY r.active, r.date DESC")
    Set<Review> getAll();

    @Query("SELECT r FROM Review r " +
            "LEFT OUTER JOIN FETCH r.user " +
            "LEFT OUTER JOIN FETCH r.likes " +
            "WHERE r.id =:id " +
            "ORDER BY r.active, r.date DESC ")
    Review getWithLikesAndUser(int id);
}