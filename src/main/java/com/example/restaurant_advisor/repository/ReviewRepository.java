package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT NEW com.example.restaurant_advisor.model.dto.ReviewDto( " +
            "r, " +
            "COUNT(rl), " +
            "SUM(case when rl =:user then 1 else 0 end) > 0" +
            ") " +
            "FROM Review r " +
            "LEFT OUTER JOIN r.user ru " +
            "LEFT OUTER JOIN r.likes rl " +
            "GROUP BY r " +
            "ORDER BY r.active, r.date DESC ")
    Page<ReviewDto> getAllReviews(Pageable pageable, @Param("user") User user);

    @Query("SELECT r FROM Review r " +
            "LEFT OUTER JOIN FETCH r.user " +
            "LEFT OUTER JOIN FETCH r.likes " +
            "WHERE r.id =:id " +
            "ORDER BY r.active, r.date DESC ")
    Review getWithLikesAndUser(int id);
}