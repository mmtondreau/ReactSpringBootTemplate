package org.tonberry.calories.calorieserver.events;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.controllers.MockitoUnitTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationStartupTest extends MockitoUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    ApplicationStartup applicationStartup;

    @Test
    void applicationReadyEvent_createUser() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        applicationStartup.applicationReadyEvent();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void applicationReadyEvent_exists() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        applicationStartup.applicationReadyEvent();
        verify(userRepository, never()).save(any(User.class));
    }
}