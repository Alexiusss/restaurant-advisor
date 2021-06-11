package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  new AuthUser(userRepository.findByEmailIgnoreCase(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("User '" + username + "' was not found")
        ));
    }
}