package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository  extends JpaRepository<User, Integer> {

    Optional<User> findByEmailIgnoreCase(String email);

    User findByActivationCode(String code);
}