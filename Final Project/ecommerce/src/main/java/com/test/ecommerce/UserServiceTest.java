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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("1234");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encoded123");
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        verify(userRepository).save(any());
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));
        when(encoder.matches(any(), any())).thenReturn(true);

        User result = userService.login("test@mail.com", "1234");

        assertNotNull(result);
    }
}
