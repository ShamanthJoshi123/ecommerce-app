package com.test.ecommerce.user.service;

import com.test.ecommerce.user.exception.UserException;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Set<Long> loggedInUsers = new HashSet<>();

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public User register(User user) {

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return repository.save(user);
    }

    @Override
    public User login(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new UserException("Invalid credentials");
        }
        loggedInUsers.add(user.getId());
        return user;
    }
    @Override
    public boolean isUserLoggedIn(Long userId) {
        return loggedInUsers.contains(userId);
    }

    @Override
    public User getUser(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));
    }
}