package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.example.restaurant_advisor.util.UserUtil.prepareToSave;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailSender mailSender;

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByEmailIgnoreCase(user.getEmail().toLowerCase());

        if (userFromDb.isPresent()) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(prepareToSave(user));

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Restaurant Advisor. Please visit next link: localhost:8080/activate/%s",
                    user.getFirstName(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthUser(userRepository.findByEmailIgnoreCase(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("User '" + username + "' was not found")
        ));
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }
}