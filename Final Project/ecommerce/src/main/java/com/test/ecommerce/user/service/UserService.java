package com.test.ecommerce.user.service;

import com.test.ecommerce.user.model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public interface UserService {

    User register(User user);

    User login(String email, String password);

    User getUser(Long id);

    boolean isUserLoggedIn(Long userId);
}