package com.example.restaurant_advisor.service;

import com.example.restaurant_advisor.model.Role;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static com.example.restaurant_advisor.UserTestUtil.NEW_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailSender mailSender;

    @Test
    void addUser() {
        User user = NEW_USER;

        boolean isUserCreated = userService.addUser(user);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        verify(userRepository, times(1)).save(user);
        verify(mailSender, times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    void addFailUserTest() {

        User user = new User();

        user.setEmail("test@mail.ru");

        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmailIgnoreCase("test@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        assertFalse(isUserCreated);

        verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(0)).send(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("Test activation");

        doReturn(user)
                .when(userRepository)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate");

        assertFalse(isUserActivated);

        verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
    }
}