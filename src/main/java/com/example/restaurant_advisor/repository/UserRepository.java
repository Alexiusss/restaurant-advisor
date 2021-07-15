package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository  extends JpaRepository<User, Integer> {

    @Override
    @Modifying
    @Transactional
    User save(User user);

    Optional<User> findByEmailIgnoreCase(String email);

    User findByActivationCode(String code);

    @EntityGraph(attributePaths = {"reviews", "subscribers", "subscriptions"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u where u.id=?1")
    Optional<User> getWithReviewsAndSubscriptionsAndSubscribers(int userId);
}