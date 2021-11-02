package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.AuthUser;
import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import com.example.restaurant_advisor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.restaurant_advisor.util.ControllerUtils.*;
import static com.example.restaurant_advisor.util.UserUtil.prepareToSave;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.assureIdConsistent;
import static com.example.restaurant_advisor.util.validation.ValidationUtil.checkNew;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailSender mailSender;

    @Value("${myhostname}")
    private String myhostname;

    @CachePut(value = "users", key = "#user.email")
    public boolean registerUser(User user) {
        log.info("register {}", user);
        checkNew(user);
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        if (userRepository.save(prepareToSave(user)) != null) {
            sendMessage(user);
            return true;
        } else {
            return false;
        }
    }

    @CachePut(value = "users", key = "#user.email")
    public User addUser(User user, Map<String, String> form) {
        log.info("create {}", user);
        checkNew(user);
        user.setActive(true);
        setRoles(user, form);
        return userRepository.save(prepareToSave(user));
    }

    @Transactional
    @CachePut(value = "users", key = "#user.email")
    public void updateUser(User user, Map<String, String> form) {

        assureIdConsistent(user, user.id());
        log.info("update {} with id={}", user, user.id());

        User updated = userRepository.getExisted(user.id());
        updated.setFirstName(user.getFirstName());
        updated.setLastName(user.getLastName());
        updated.setEmail(user.getEmail());
        updated.setPassword(user.getPassword());
        setRoles(updated, form);

        prepareAndSave(updated);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthUser(userRepository.findByEmailIgnoreCase(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("User '" + username + "' was not found")
        ));
    }

    private void setRoles(User user, Map<String, String> form) {
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

    private void sendMessage(User user) {
        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Restaurant Advisor. Please visit next link: <a href='http://%s/user/activate/%s'>link</a>",
                    user.getFirstName(),
                    myhostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    @Transactional
    public void subscribe(AuthUser currentUser, int userId) {
        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        log.info("user {} subscribe to user {}", currentUser, user);
        user.getSubscribers().add(currentUser.getUser());
    }

    @Transactional
    public void unsubscribe(AuthUser currentUser, int userId) {
        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        log.info("user {} unsubscribe to user {}", currentUser, user);
        user.getSubscribers().remove(currentUser.getUser());
    }

    @Transactional
    public void activate(int id, boolean enabled) {
        User user = userRepository.getExisted(id);
        user.setActive(enabled);
    }

    public void prepareAndSave(User user) {
        userRepository.save(prepareToSave(user));
    }

    public void getUserReviews(int userId, AuthUser currentUser, Model model, Pageable pageable) {

        User user = userRepository.getWithReviewsAndSubscriptionsAndSubscribers(userId).orElseThrow();
        Set<Review> reviews = user.getReviews();

        if (!currentUser.getUser().getRoles().contains(Role.ADMIN) && currentUser.getUser().id() != userId) {
            reviews = getActiveReviews(reviews);
        }

        Page<ReviewDto> page = createPageFromList(pageable, createListReviewTos(reviews, currentUser.getUser()));

        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser.getUser()));
        model.addAttribute("page", page);
        model.addAttribute("url", "/user-reviews/" + currentUser.id());
        model.addAttribute("isCurrentUser", currentUser.getUser().equals(user));
    }
}