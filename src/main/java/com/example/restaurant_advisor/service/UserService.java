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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

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
                            "Welcome to Restaurant Advisor. Please visit next link: <a href='http://localhost:8080/user/activate/%s'>link</a>",
                    user.getFirstName(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    @Transactional
    public void updateUser(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        Set<Role> userRoles = new HashSet<>();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                userRoles.add(Role.valueOf(key));
            }
        }
        user.setRoles(userRoles);

        prepareAndSave(user);
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

    @Transactional
    public void subscribe(AuthUser currentUser, User user) {
        user.getSubscribers().add(currentUser.getUser());
        userRepository.save(user);
    }

    @Transactional
    public void unsubscribe(AuthUser currentUser, User user) {
        user.getSubscribers().remove(currentUser.getUser());
        userRepository.save(user);
    }

    @Transactional
    public void activate(int id, boolean enabled) {
        User user = userRepository.getOne(id);
        user.setActive(enabled);
    }

    public void prepareAndSave(User user) {
        userRepository.save(prepareToSave(user));
    }
}