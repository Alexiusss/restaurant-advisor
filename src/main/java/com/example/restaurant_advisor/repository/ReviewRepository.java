package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.id=:id AND r.user.id=:userId")
    int delete(int id, int userId);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Review r WHERE r.user.id=?1" )
    List<Review> getAllByUserId(int id);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Review r ORDER BY r.active, r.date DESC ")
    List<Review> getAllReviews();
}