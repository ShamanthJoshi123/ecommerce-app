package com.test.ecommerce;

import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;
import com.test.ecommerce.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encrypted_pass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("encrypted_pass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("password123", "encrypted_pass")).thenReturn(true);

        User result = userService.login("test@mail.com", "password123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}