package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Integer> {
}